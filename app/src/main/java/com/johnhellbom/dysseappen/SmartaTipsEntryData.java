package com.johnhellbom.dysseappen;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by John Hellbom on 2016-02-29.
 */
public class SmartaTipsEntryData implements Parcelable {

    public int id;
    public String header;
    public String videoUrl;
    public ArrayList tips = new ArrayList();
    public ArrayList tipsStatus = new ArrayList();

    public SmartaTipsEntryData(int id, String header, String videoUrl) {
        this.id = id;
        this.header = header;
        this.videoUrl = videoUrl;
    }

    protected SmartaTipsEntryData(Parcel in) {
        id = in.readInt();
        header = in.readString();
        videoUrl = in.readString();
        if (in.readByte() == 0x01) {
            tips = new ArrayList<>();
            in.readList(tips, SmartaTipsEntry.class.getClassLoader());
        } else {
            tips = null;
        }
        if (in.readByte() == 0x01) {
            tipsStatus = new ArrayList<>();
            in.readList(tipsStatus, SmartaTipsEntry.class.getClassLoader());
        } else {
            tipsStatus = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(header);
        dest.writeString(videoUrl);
        if (tips == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(tips);
        }
        if (tipsStatus == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(tipsStatus);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SmartaTipsEntryData> CREATOR = new Parcelable.Creator<SmartaTipsEntryData>() {
        @Override
        public SmartaTipsEntryData createFromParcel(Parcel in) {
            return new SmartaTipsEntryData(in);
        }

        @Override
        public SmartaTipsEntryData[] newArray(int size) {
            return new SmartaTipsEntryData[size];
        }
    };
}