package com.example.axosnet.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Receipt implements Parcelable {
    private int id;
    private Double amount;
    private String provider;
    private String emission_date;
    private String comment;
    private String currency_code;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getEmission_date() {
        return emission_date;
    }

    public void setEmission_date(String emission_date) {
        this.emission_date = emission_date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeDouble(amount);
        parcel.writeString(provider);
        parcel.writeString(emission_date);
        parcel.writeString(comment);
        parcel.writeString(currency_code);
    }

    protected Receipt(Parcel in) {
        id = in.readInt();
        amount = in.readDouble();
        provider = in.readString();
        emission_date = in.readString();
        comment = in.readString();
        currency_code = in.readString();
    }

    public static final Creator<Receipt> CREATOR = new Creator<Receipt>() {
        @Override
        public Receipt createFromParcel(Parcel in) {
            return new Receipt(in);
        }

        @Override
        public Receipt[] newArray(int size) {
            return new Receipt[size];
        }
    };


}
