package me.jezza.oc.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezza.oc.client.ClientUtil;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ItemInformation {

    private ArrayList<String> infoList = new ArrayList<String>();
    private ArrayList<String> shiftList = new ArrayList<String>();

    public void addToList(List list) {
        if (!shiftList.isEmpty() && ClientUtil.hasPressedShift())
            list.addAll(shiftList);
        else
            list.addAll(infoList);
    }

    public void defaultInfoList() {
        infoList.add("Press" + EnumChatFormatting.DARK_RED + " Shift" + EnumChatFormatting.GRAY + " for more info.");
    }

    public void addToBothLists(String string) {
        infoList.add(string);
        shiftList.add(string);
    }

    public void addShiftList(String string) {
        shiftList.add(string);
    }

    public void addInfoList(String string) {
        infoList.add(string);
    }
}