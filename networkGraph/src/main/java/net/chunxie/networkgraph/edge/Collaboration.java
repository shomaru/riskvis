package net.chunxie.networkgraph.edge;

import net.chunxie.networkdata.entity.Contributor;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 08/02/13 15:42
 */
public class Collaboration {

    private Contributor contributorA;
    private Contributor contributorB;

    private Integer amount = 0;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Contributor getContributorA() {
        return contributorA;
    }

    public void setContributorA(Contributor contributorA) {
        this.contributorA = contributorA;
    }

    public Contributor getContributorB() {
        return contributorB;
    }

    public void setContributorB(Contributor contributorB) {
        this.contributorB = contributorB;
    }
}