package com.example.facialasymmetryandroid.model

import android.os.Parcel
import android.os.Parcelable

data class ReturnString(
    var status: String?,
    var message: String?,
    var imageBytes: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(status)
        parcel.writeString(message)
        parcel.writeString(imageBytes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ReturnString> {
        override fun createFromParcel(parcel: Parcel): ReturnString {
            return ReturnString(parcel)
        }

        override fun newArray(size: Int): Array<ReturnString?> {
            return arrayOfNulls(size)
        }
    }
}
