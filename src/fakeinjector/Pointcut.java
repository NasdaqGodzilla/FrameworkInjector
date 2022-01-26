package peacemaker.frameworkinjector;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "targetClass",
    "targetMethod",
    "insertBefore",
    "insertAfter",
    "enable",
    "version",
    "description",
    "targetParams"
})
@Generated("jsonschema2pojo")
public class Pointcut implements Serializable {
    @JsonProperty("targetClass")
    private String targetClass;
    @JsonProperty("targetMethod")
    private String targetMethod;
    @JsonProperty("insertBefore")
    private String insertBefore;
    @JsonProperty("insertAfter")
    private String insertAfter;
    @JsonProperty("enable")
    private Boolean enable;
    @JsonProperty("version")
    private Integer version;
    @JsonProperty("description")
    private String description;
    @JsonProperty("targetParams")
    private List<String> targetParams = null;
    private final static long serialVersionUID = 2573282971151864416L;

    @JsonProperty("targetClass")
    public String getTargetClass() {
        return targetClass;
    }

    @JsonProperty("targetClass")
    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }

    @JsonProperty("targetMethod")
    public String getTargetMethod() {
        return targetMethod;
    }

    @JsonProperty("targetMethod")
    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
    }

    @JsonProperty("insertBefore")
    public String getInsertBefore() {
        return insertBefore;
    }

    @JsonProperty("insertBefore")
    public void setInsertBefore(String insertBefore) {
        this.insertBefore = insertBefore;
    }

    @JsonProperty("insertAfter")
    public String getInsertAfter() {
        return insertAfter;
    }

    @JsonProperty("insertAfter")
    public void setInsertAfter(String insertAfter) {
        this.insertAfter = insertAfter;
    }

    @JsonProperty("enable")
    public Boolean getEnable() {
        return enable;
    }

    @JsonProperty("enable")
    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    @JsonProperty("version")
    public Integer getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(Integer version) {
        this.version = version;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("targetParams")
    public List<String> getTargetParams() {
        return targetParams;
    }

    @JsonProperty("targetParams")
    public void setTargetParams(List<String> targetParams) {
        this.targetParams = targetParams;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Pointcut.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("targetClass");
        sb.append('=');
        sb.append(((this.targetClass == null)?"<null>":this.targetClass));
        sb.append(',');
        sb.append("targetMethod");
        sb.append('=');
        sb.append(((this.targetMethod == null)?"<null>":this.targetMethod));
        sb.append(',');
        sb.append("insertBefore");
        sb.append('=');
        sb.append(((this.insertBefore == null)?"<null>":this.insertBefore));
        sb.append(',');
        sb.append("insertAfter");
        sb.append('=');
        sb.append(((this.insertAfter == null)?"<null>":this.insertAfter));
        sb.append(',');
        sb.append("enable");
        sb.append('=');
        sb.append(((this.enable == null)?"<null>":this.enable));
        sb.append(',');
        sb.append("version");
        sb.append('=');
        sb.append(((this.version == null)?"<null>":this.version));
        sb.append(',');
        sb.append("description");
        sb.append('=');
        sb.append(((this.description == null)?"<null>":this.description));
        sb.append(',');
        sb.append("targetParams");
        sb.append('=');
        sb.append(((this.targetParams == null)?"<null>":this.targetParams));
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
        result = ((result* 31)+((this.targetMethod == null)? 0 :this.targetMethod.hashCode()));
        result = ((result* 31)+((this.targetClass == null)? 0 :this.targetClass.hashCode()));
        result = ((result* 31)+((this.enable == null)? 0 :this.enable.hashCode()));
        result = ((result* 31)+((this.description == null)? 0 :this.description.hashCode()));
        result = ((result* 31)+((this.targetParams == null)? 0 :this.targetParams.hashCode()));
        result = ((result* 31)+((this.version == null)? 0 :this.version.hashCode()));
        result = ((result* 31)+((this.insertAfter == null)? 0 :this.insertAfter.hashCode()));
        result = ((result* 31)+((this.insertBefore == null)? 0 :this.insertBefore.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Pointcut) == false) {
            return false;
        }
        Pointcut rhs = ((Pointcut) other);
        return (((((((((this.targetMethod == rhs.targetMethod)||((this.targetMethod!= null)&&this.targetMethod.equals(rhs.targetMethod)))&&((this.targetClass == rhs.targetClass)||((this.targetClass!= null)&&this.targetClass.equals(rhs.targetClass))))&&((this.enable == rhs.enable)||((this.enable!= null)&&this.enable.equals(rhs.enable))))&&((this.description == rhs.description)||((this.description!= null)&&this.description.equals(rhs.description))))&&((this.targetParams == rhs.targetParams)||((this.targetParams!= null)&&this.targetParams.equals(rhs.targetParams))))&&((this.version == rhs.version)||((this.version!= null)&&this.version.equals(rhs.version))))&&((this.insertAfter == rhs.insertAfter)||((this.insertAfter!= null)&&this.insertAfter.equals(rhs.insertAfter))))&&((this.insertBefore == rhs.insertBefore)||((this.insertBefore!= null)&&this.insertBefore.equals(rhs.insertBefore))));
    }
}

