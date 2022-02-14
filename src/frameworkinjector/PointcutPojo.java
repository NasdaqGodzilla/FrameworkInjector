package peacemaker.frameworkinjector;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "pointcuts"
})
@Generated("jsonschema2pojo")
public class PointcutPojo implements Serializable {
    @JsonProperty("pointcuts")
    private List<Pointcut> pointcuts = null;
    private final static long serialVersionUID = 995177083557960996L;

    @JsonProperty("pointcuts")
    public List<Pointcut> getPointcuts() {
        return pointcuts;
    }

    @JsonProperty("pointcuts")
    public void setPointcuts(List<Pointcut> pointcuts) {
        this.pointcuts = pointcuts;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PointcutPojo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("pointcuts");
        sb.append('=');
        sb.append(((this.pointcuts == null)?"<null>":this.pointcuts));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.pointcuts == null)? 0 :this.pointcuts.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PointcutPojo) == false) {
            return false;
        }
        PointcutPojo rhs = ((PointcutPojo) other);
        return ((this.pointcuts == rhs.pointcuts)||((this.pointcuts!= null)&&this.pointcuts.equals(rhs.pointcuts)));
    }
}

