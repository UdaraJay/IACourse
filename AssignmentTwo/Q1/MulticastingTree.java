import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by noMoon on 2015-02-24.
 */
public class MulticastingTree extends Algorithm {

    public MulticastingTree() {

        super();
    }


    @Override
    public void run() {

        multicastingTree(getID(), 3);
    }

    public void multicastingTree(String id, int k) {
        Message mssg;
        boolean inTree = false;
        Vector adjacent = neighbours();
        String parent = null;
        if (isRoot()) {
            inTree = true;
            parent = "GOD";
            mssg = new Message(adjacent, id, "?");
        } else {
            mssg = null;
        }
        List<Message> ack_list = new ArrayList<Message>();
        String temp_parent = parent;

        List<String> children = new ArrayList<String>();
        int rounds_left = -1;
        int number_rejection = 0, number_ack = 0;


        try {
            while (true) {
                waitForNextRound();
                if (mssg != null) {
                    send(mssg);
                    mssg = null;
                }
                if (ack_list.size() > 0) {
                    for (Message ack : ack_list) {
                        send(ack);
                    }
                    ack_list.clear();
                }


                Message m = receive();

                while (m != null) {
                    if (m.data().equals("?")) {
                        if (null == temp_parent) {
                            temp_parent = m.fromId;
                            Vector target = neighbours();
                            target.removeElement(temp_parent);
                            mssg = new Message(target, id, "?");
                            if (neighbours().size() == k) {
                                parent = temp_parent;
                                inTree = true;
                                ack_list.add(new Message(temp_parent, id, "ACK,YES"));
                            }else if (neighbours().size()==1){
                                ack_list.add(new Message(temp_parent, id, "ACK,NO"));
                            }
                        } else {
                            ack_list.add(new Message(m.fromId, id, "ACK,NO"));
                        }
                    } else if (m.data().startsWith("ACK")) {
                        String[] dataList = m.data().split(",");
                        number_ack++;
                        if (dataList[1].equals("YES")) {
                            children.add(m.fromId);
                            if (!inTree) {
                                parent = temp_parent;
                                inTree = true;
                                ack_list.add(new Message(temp_parent, id, "ACK,YES"));
                            }
                        } else {
                            number_rejection++;
                            if (number_rejection == neighbours().size() - 1) {
                                if (!inTree) {
                                    ack_list.add(new Message(temp_parent, id, "ACK,NO"));
                                }
                            }
                        }
                    }
                    m = receive();
                }
                if (rounds_left == 1) {
                    printMessage(String.valueOf(inTree));
                    return;
                }
                if (isRoot()) {
                    if (number_ack == neighbours().size()) {
                        rounds_left = 1;
                    }
                } else if(temp_parent!=null){
                    if (number_ack == neighbours().size() - 1) {
                        rounds_left = 1;
                    }
                }
            }
        } catch (SimulatorException e) {
            e.printStackTrace();
        }
    }
}
