package me.jezza.oc.common.core.channel;

@Deprecated
public class MessageGuiNotify {//  extends NetworkDispatcher.MessageAbstract<MessageGuiNotify, IMessage> {

//    private int id, process;
//
//    public MessageGuiNotify() {
//    }
//
//    /**
//     * @param id The ID of the button.
//     * @param process The ID of the process to be executed.
//     */
//    public MessageGuiNotify(int id, int process) {
//        this.id = id;
//        this.process = process;
//    }
//
//    @Override
//    public void fromBytes(ByteBuf buf) {
//        id = buf.readInt();
//        process = buf.readInt();
//    }
//
//    @Override
//    public void toBytes(ByteBuf buf) {
//        buf.writeInt(id);
//        buf.writeInt(process);
//    }
//
//    @Override
//    public IMessage onMessage(MessageGuiNotify message, MessageContext ctx) {
//        EntityPlayer player = ctx.getServerHandler().playerEntity;
//        if (player.openContainer instanceof IGuiMessageHandler)
//            ((IGuiMessageHandler) player.openContainer).onClientClick(player, message.id, message.process);
//        return null;
//    }

}