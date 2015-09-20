package me.jezza.oc.common.interfaces;

import java.util.Collection;
import java.util.List;

public interface IItemTooltip {

    /**
     * Used to actually add the information to a list.
     * @param list - the list to populate.
     */
    void populateList(List<String> list);

    /**
     * Used to set a default non-shift list.
     * Such as: "Press SHIFT for more." 
     */
    void defaultInfoList();

    /**
     * Adds the text to both the information list, and the shift list.  
     * @param string - String to add
     */
    void addToBothLists(String string);

    /**
     * Adds the text to the information list. 
     * @param string - String to add
     */
    void addToInfoList(String string);

    /**
     * Add the collection of strings to add to the information list.
     * @param strings - Strings to add
     */
    void addAllToInfoList(String... strings);

    /**
     * Add the collection of strings to add to the information list.
     * @param strings - Strings to add
     */
    void addAllToInfoList(Collection<String> strings);

    /**
     * Adds the text to add to the shift list.
     * @param string - String to add
     */
    void addToShiftList(String string);

    /**
     * Add the collection of strings to add to the shift list.
     * @param strings - Strings to add
     */
    void addAllToShiftList(String... strings);

    /**
     * Add the collection of strings to add to the shift list.
     * @param strings - Strings to add
     */
    void addAllToShiftList(Collection<String> strings);
}
