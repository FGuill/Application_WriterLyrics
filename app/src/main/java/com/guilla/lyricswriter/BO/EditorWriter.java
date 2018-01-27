package com.guilla.lyricswriter.BO;

/**
 * Created by Moi on 21/11/15.
 */
public class EditorWriter {

    //private variables
    public String writerid;
    public String writername;
    public String writerpicprofil;
    public String writertextline;
    public String writernbline;
    public String writertotalline;


    // Empty constructor
    public EditorWriter(){

    }

    public EditorWriter(String writerid, String writername, String writerpicprofil, String writertextline, String writernbline,String writertotalline){
        this.writerid = writerid;
        this.writername = writername;
        this.writerpicprofil=writerpicprofil;
        this.writertextline = writertextline;
        this.writernbline=writernbline;
        this.writertotalline=writertotalline;
    }

    public EditorWriter(String writerid, String writername, String writertextline, String writernbline){
        this.writerid = writerid;
        this.writername = writername;
        this.writertextline = writertextline;
        this.writernbline=writernbline;
    }

    // constructor


    public String getWriterid() {
        return writerid;
    }

    public String getWritername() {
        return writername;
    }

    public String getWriternbline() {
        return writernbline;
    }

    public String getWriterpicprofil() {
        return writerpicprofil;
    }

    public String getWritertextline() {
        return writertextline;
    }

    public String getWritertotalline() {
        return writertotalline;
    }
}
