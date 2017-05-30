package br.asha.dfss.view;

import br.asha.dfss.hub.NodeHub;
import br.asha.dfss.model.SharedFile;
import br.asha.dfss.repository.SharedFileList;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashSet;

public class LogadoScreen extends BaseScreen implements ActionListener {

    private JList<SharedFile> mListaArquivos = new JList<>();
    private JButton mDownloadButton = new JButton("DOWNLOAD");
    private JButton mUploadButton = new JButton("UPLOAD");
    private JButton mAtualizarButton = new JButton("ATUALIZAR");
    private NodeHub mHub;

    public LogadoScreen(NodeHub hub) {
        mHub = hub;

        mDownloadButton.addActionListener(this);
        mUploadButton.addActionListener(this);
        mAtualizarButton.addActionListener(this);

        add(mAtualizarButton, WIDTH / 2 + 125, 50, 150, 50);
        add(mListaArquivos, WIDTH / 2, 230, 400, 300);
        add(mDownloadButton, WIDTH / 2 - 75, 430, 150, 50);
        add(mUploadButton, WIDTH / 2 + 75, 430, 150, 50);

        exibir();
    }

    public static void main(String[] args) {
        new LogadoScreen(null);
    }

    @Override
    public void dispose() {
        super.dispose();
        mHub.vouDesligar();
        new InicioScreen();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final Object o = e.getSource();
        //Download do arquivo selecionado.
        if (o == mDownloadButton) {
            final SharedFile arquivoSelecionado = mListaArquivos.getSelectedValue();
            if (arquivoSelecionado != null) {
                //Baixa o arquivo selecionado.
                byte[] data = mHub.queroOArquivo(arquivoSelecionado);
                //Tentar de outra maquina.
                SharedFileList outraLista;
                if (data == null &&
                        (outraLista = mHub.queroAListaDeArquivosCompartilhados()) != null) {
                    for (SharedFile file : outraLista) {
                        //Arquivos com mesmo nome e conteúdo mas ip diferentes.
                        if (file.sha.equals(arquivoSelecionado.sha) &&
                                !file.ip.equals(arquivoSelecionado.ip)) {
                            data = mHub.queroOArquivo(file);
                            if (data != null) break;
                        }
                    }
                }

                //Salvar os dados.
                if (data != null) {
                    JFileChooser fileChooser = new JFileChooser("Salvar");
                    //Escolhe o arquivo para salvar.
                    int opcao = fileChooser.showSaveDialog(this);
                    if (opcao == JFileChooser.APPROVE_OPTION) {
                        final File file = fileChooser.getSelectedFile();
                        try (OutputStream os = new FileOutputStream(file)) {
                            os.write(data);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(this, "Não foi possível salvar o arquivo");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Não foi possível realizar o download");
                }
            }
        }
        //Upload de um arquivo do computador.
        else if (o == mUploadButton) {
            JFileChooser fileChooser = new JFileChooser("Upload");
            //Escolhe o arquivo para o upload.
            int opcao = fileChooser.showOpenDialog(this);
            if (opcao == JFileChooser.APPROVE_OPTION) {
                final File file = fileChooser.getSelectedFile();
                if (mHub.queroCompartilharUmArquivo(file)) {
                    JOptionPane.showMessageDialog(null, "Este arquivo está agora "
                                    + "disponível para compartilhamento", "Sucesso!",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Algo errado aconteceu! \n"
                                    + "Não foi possível compartilhar este arquivo",
                            "ERRO!", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        //Buscar a lista de arquivos disponiveis.
        else if (o == mAtualizarButton) {
            DefaultListModel<SharedFile> model = new DefaultListModel<>();
            HashSet<String> files = new HashSet<>();
            //Exibe apenas os nomes dos arquivos
            for (SharedFile file : mHub.queroAListaDeArquivosCompartilhados()) {
                final String name = new File(file.nome).getName();
                if (files.contains(name)) {
                    files.add(name);
                    model.addElement(file);
                }
            }

            mListaArquivos.setModel(model);
        }
    }
}
