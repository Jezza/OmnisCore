package me.jezza.oc.common.items;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezza.oc.client.ClientUtil;
import me.jezza.oc.common.interfaces.IItemTooltip;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static me.jezza.oc.common.utils.helpers.StringHelper.translate;

@SideOnly(Side.CLIENT)
public class ItemInformation implements IItemTooltip {

    private ArrayList<String> infoList = new ArrayList<String>();
    private ArrayList<String> shiftList = new ArrayList<String>();

    public void populateList(List<String> list) {
        if (!shiftList.isEmpty() && ClientUtil.hasPressedShift())
            list.addAll(shiftList);
        else
            list.addAll(infoList);
    }

    public void defaultInfoList() {
        infoList.add(translate("info.tooltip.default.shift"));
    }

    public void addToBothLists(String string) {
        infoList.add(string);
        shiftList.add(string);
    }

    public void addToInfoList(String string) {
        infoList.add(string);
    }

    public void addAllToInfoList(String... strings) {
        infoList.addAll(Lists.newArrayList(strings));
    }

    public void addAllToInfoList(Collection<String> strings) {
        infoList.addAll(strings);
    }

    public void addToShiftList(String string) {
        shiftList.add(string);
    }

    public void addAllToShiftList(String... strings) {
        shiftList.addAll(Lists.newArrayList(strings));
    }

    public void addAllToShiftList(Collection<String> strings) {
        shiftList.addAll(strings);
    }
}