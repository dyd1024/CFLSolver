package org.CFLGraph;

public class EdgeNode {
    private String weight;
    private String targetNode;

    public EdgeNode(String weight, String targetNode){
        this.weight = weight;
        this.targetNode = targetNode;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getTargetNode() {
        return targetNode;
    }

    public void setTargetNode(String targetNode) {
        this.targetNode = targetNode;
    }
}
