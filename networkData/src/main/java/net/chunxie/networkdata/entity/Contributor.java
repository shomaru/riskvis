package net.chunxie.networkdata.entity;

import java.io.Serializable;

/**
 * @author chun.xie
 * @todo Write some documentation!
 * @created 07/02/13 13:20
 */
public class Contributor implements Serializable {
    private static final long serialVersionUID = 1L;

    private String contributor;

    private Double points;

    private Integer partialPoints = 0;

    public Contributor(String contributor) {
        this.contributor = contributor;
    }

    public String getContributor() {
        return contributor;
    }

    public void setContributor(String contributor) {
        this.contributor = contributor;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public Integer getPartialPoints() {
        return partialPoints;
    }

    public void setPartialPoints(Integer partialPoints) {
        this.partialPoints = partialPoints;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (contributor != null ? contributor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Contributor)) {
            return false;
        }
        Contributor other = (Contributor) object;
        if ((this.contributor == null && other.contributor != null) || (this.contributor != null && !this.contributor.equals(other.contributor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return contributor.toString();
    }

}