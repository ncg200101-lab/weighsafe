package com.weigh.safemobile.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class NotesModel extends RealmObject {
    @PrimaryKey
    private int nRealmId;
    private int nVehicleId; // primary key of Vehicle Model
    private String notes;

    public NotesModel()
    {
        this.nRealmId = 0;
        this.nVehicleId = 0;
        this.notes = "";
    }

    public NotesModel(NotesModel s)
    {
        this.nRealmId = s.nRealmId;
        this.nVehicleId = s.nVehicleId;
        this.notes = s.notes;
    }

    public int getnRealmId() {
        return nRealmId;
    }

    public void setnRealmId(int nRealmId) {
        this.nRealmId = nRealmId;
    }

    public int getnVehicleId() {
        return nVehicleId;
    }

    public void setnVehicleId(int nVehicleId) {
        this.nVehicleId = nVehicleId;
    }

    public String getNotes() { return notes; }

    public void setNotes(String notes) { this.notes = notes; }
}
