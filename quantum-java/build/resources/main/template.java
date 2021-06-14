package _PACKAGE_;

import art.arcane.quantum.QuantumFabric;
import art.arcane.quantum.api.QuantumEntangledObject;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
//imports

/**
 * This class was generated by Quantum
 */
@Data
@Builder
@EqualsAndHashCode
@QuantumEntangledObject("_CANONICAL_NAME_")
public class _NAME_ implements Cloneable
{
//fields
//methods
    /**
     * Create a new _NAME_ from supplied json
     * @param json the json
     * @return the new _NAME_ object
     */
    public static _NAME_ fromJson(String json)
    {
        return QuantumFabric.gson.fromJson(json, _NAME_.class);
    }

    /**
     * Convert this _NAME_ to json (serialize)
     * @return the json string
     */
    public String toJson()
    {
        return QuantumFabric.gson.toJson(this);
    }

    /**
     * Clones this _NAME_ by serializing to json, then deserializing.
     * @return the cloned object
     */
    public _NAME_ secureClone()
    {
        return fromJson(toJson());
    }
}