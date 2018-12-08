package com.jman.capstone_project.remoteDataSource;

/*
 * This class acts as an interface between the AsyncTask and The UI.
 * onResultReceived is called at onPostExecute to tell the callback object that the AsyncTask
 * has finished and has received a result.
 * This allows de-coupling of code between the AsyncTask and The UI.
 * */
public interface IAsyncTaskCallback {

    void onResultReceived(String result);
}
