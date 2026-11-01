// Mod by Nixz • Dev
// IKsuInterface.aidl
package com.kimo.nixz;

import android.content.pm.PackageInfo;
import rikka.parcelablelist.ParcelableListSlice;

interface IKsuInterface {
    ParcelableListSlice<PackageInfo> getPackages(int flags);
}