package test.jezza.me;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import me.jezza.oc.common.items.ItemAbstract;
import net.minecraft.creativetab.CreativeTabs;

/**
 * @author Jezza
 */
@Mod(modid = "TestMod")
public class TestMod {
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		new TestItem();
	}
	public static class TestItem extends ItemAbstract {
		public TestItem() {
			super("TestItem");
			textureless(true);
			setCreativeTab(CreativeTabs.tabMisc);
		}
	}
}
