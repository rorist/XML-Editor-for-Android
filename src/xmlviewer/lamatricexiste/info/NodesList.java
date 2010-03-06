package xmlviewer.lamatricexiste.info;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class NodesList extends ArrayList<Node> implements Parcelable {

    private static final long serialVersionUID = 663585476779879096L;

    public NodesList() {
    }

    public NodesList(Parcel in) {
        readFromParcel(in);
    }

    @SuppressWarnings("unchecked")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public NodesList createFromParcel(Parcel in) {
            return new NodesList(in);
        }

        public Object[] newArray(int arg0) {
            return null;
        }

    };

    @SuppressWarnings("unchecked")
    private void readFromParcel(Parcel in) {
        this.clear();

        // Read the list size
        int size = in.readInt();

        // Order is fundamental
        for (int i = 0; i < size; i++) {
            Node n = new Node();
            n.name = in.readString();
            n.content = in.readString();
            n.attrs = in.readHashMap(null);
            n.childs = in.readArrayList(null);
            this.add(n);
        }

    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        int size = this.size();
        // Write the list size
        dest.writeInt(size);
        // Write the info
        for (int i = 0; i < size; i++) {
            Node n = this.get(i);
            dest.writeString(n.name);
            dest.writeString(n.content);
            dest.writeMap(n.attrs);
            dest.writeArray(getChildsArray(n.childs));
        }
    }

    private String[] getChildsArray(ArrayList<Node> childs) {
        String[] c = new String[childs.size()];
        // c[0] = "prout";
        for (int i = 0; i < childs.size(); i++) {
            c[i] = childs.get(i).name;
        }
        return c;
    }

}
