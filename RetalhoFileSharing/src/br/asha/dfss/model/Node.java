package br.asha.dfss.model;

import java.io.Serializable;

public class Node implements Serializable {
    private String mIp;
    private String mSubnetName;

    public Node(String ip, String subnetName) {
        mIp = ip;
        mSubnetName = subnetName;
    }

    public String getIp() {
        return mIp;
    }

    public void setIp(String ip) {
        mIp = ip;
    }

    public String getSubnetName() {
        return mSubnetName;
    }

    @Override
    public int hashCode() {
        return getSubnetName().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Node &&
                ((Node) o).getSubnetName().equals(getSubnetName());
    }
}
