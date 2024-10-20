package com.example.myapplication.chat;

import android.annotation.SuppressLint;
import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

@SuppressLint("ParcelCreator")
public class ContactSearchSuggestion implements SearchSuggestion {
    private String suggestion;
    private boolean mIsHistory = false;

    public ContactSearchSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public ContactSearchSuggestion(Parcel source) {
        this.suggestion = source.readString();
        this.mIsHistory = source.readInt() != 0;
    }

    public void setIsHistory(boolean mIsHistory) {
        this.mIsHistory = mIsHistory;
    }

    public boolean getIsHistory() {
        return mIsHistory;
    }

    @Override
    public String getBody() {
        return suggestion;
    }

    public static final Creator<ContactSearchSuggestion> CREATOR = new Creator<ContactSearchSuggestion>() {
        @Override
        public ContactSearchSuggestion createFromParcel(Parcel parcel) {
            return new ContactSearchSuggestion(parcel);
        }

        @Override
        public ContactSearchSuggestion[] newArray(int i) {
            return new ContactSearchSuggestion[i];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(suggestion);
        dest.writeInt(mIsHistory ? 1: 0);
    }

    public boolean isHistory() {
        return mIsHistory; // 根据需要返回
    }
}
