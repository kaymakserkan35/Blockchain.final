package com.betelgeuse.blockchain.data.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.betelgeuse.blockchain.data.ADatabase;
import com.betelgeuse.blockchain.data.ITickerDB;
import com.betelgeuse.blockchain.data.dataListener.TickerDTOListListener;
import com.betelgeuse.blockchain.data.dataListener.TickerDTOListener;
import com.betelgeuse.blockchain.data.dto.TickerDTO;
import com.betelgeuse.blockchain.H;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TickerDBCache extends ADatabase implements ITickerDB {
    Source sourceOfCACHE = Source.CACHE;
    private FirebaseFirestore db;
    private CollectionReference tickerCollection;

    public TickerDBCache(FirebaseFirestore db) {
        this.db = db;
        tickerCollection = db.collection(TickerDTO.class.getSimpleName());
    }

    @Override // does  work
    public void readTickerOfDate(String date, String toCurrency, TickerDTOListener listener) {
        /*----------------------------------------------------------------------------*/
        Date dateObj = parseStringDateToDateObject(date);
        String tickerId = "USD" + "-" + toCurrency;
        String year = String.valueOf(dateObj.getYear());
        DocumentReference docRef =
                tickerCollection.document(tickerId).collection(year).document(date);
        docRef.get(sourceOfCACHE).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    boolean isFromCache = document.getMetadata().isFromCache(); // eger cachende soyle davran, degilse sole davran gibi gibi ...
                    if (document.exists()) {
                        TickerDTO tickerDTO = document.toObject(TickerDTO.class);
                        if (listener != null) {
                            listener.onSuccess(tickerDTO);
                        }
                        H.debugLog(this.getClass().getSimpleName(), "readTickerOfDate", document.getData().toString());
                    } else {
                        Log.d("readTickerOfDate", "No such document");
                    }
                } else {
                    // gelen error : get failed with -->Failed to get document from cache. (However, this document may exist on the server.
                    H.debugLog("readTickerOfDate", "get failed with ", task.getException().getLocalizedMessage());
                }
            }
        });
    }

    @Override
    public void readTickerOfDateLIVE(String date, String toCurrency, TickerDTOListener listener) {
        listener.onSuccess(null);
    }

    @Override // does  work
    public void readTickersOfDate(String date, TickerDTOListListener listener) {
        Date dateObj = parseStringDateToDateObject(date);
        String year = String.valueOf(dateObj.getYear());
        H.debugLog(this.getClass().getSimpleName(), "readTickersOfDate", year);
        db.collectionGroup(year).whereEqualTo("calendar", date).get(sourceOfCACHE)
                .addOnCompleteListener((Task<QuerySnapshot> task) -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                        List<TickerDTO> tickerDTOList = new ArrayList<>();
                        for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
                            tickerDTOList.add(snap.toObject(TickerDTO.class));
                            H.debugLog(this.getClass().getSimpleName(), "readTickersOfDate", snap.getData().toString());
                        }
                        if (listener != null) {
                            listener.onSuccess(tickerDTOList);
                        }
                    }
                }).addOnFailureListener(e -> {
            // gelen error : get failed with -->Failed to get document from cache. (However, this document may exist on the server.
            H.errorLog("readTickerOfDateFromCache", "", e.getLocalizedMessage().toString());
        });
    }

    @Override
    public void readTickerFromDateToNow(String toCurrency, String fromDate, @NonNull TickerDTOListListener listener) {

        Date dateObj = parseStringDateToDateObject(fromDate);
        String fromYear = String.valueOf(dateObj.getYear());
        String toYear = getYearNowOfCurrentTimeZone();
        int fromYearINT = Integer.valueOf(fromYear);
        int toYearINT = Integer.valueOf(toYear);
        List<TickerDTO> returnTickerList = new ArrayList<>();

        do {
            int finalFromYearINT = fromYearINT;
            readTickerFromDateTo_EndOfTheYear(toCurrency, fromDate, tickerList -> {
                returnTickerList.addAll(tickerList);
                if ((finalFromYearINT == toYearINT) && listener != null) {
                    listener.onSuccess(returnTickerList);
                }
            });
            fromYearINT = fromYearINT + 1;
            fromDate = fromYearINT + "-" + "01" + "-" + "01";

        } while (fromYearINT <= toYearINT);


    }

    /*--------------------------------*/
    private void readTickerFromDateTo_EndOfTheYear(String toCurrency, String fromDate, @NonNull TickerDTOListListener listener) {
        // does work ...
        String tickerId = (this.baseCurrency + "-" + toCurrency).toUpperCase();
        Date dateObj = parseStringDateToDateObject(fromDate);
        String yearSTRİNG = String.valueOf(dateObj.getYear());

        CollectionReference collRef = tickerCollection.document(tickerId).collection(yearSTRİNG);
        List<TickerDTO> tickerList = new ArrayList<>();
        collRef.whereGreaterThanOrEqualTo("calendar", fromDate).get(sourceOfCACHE).addOnCompleteListener((Task<QuerySnapshot> task) -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> dSList = task.getResult().getDocuments();
                H.debugLog(this.getClass().getSimpleName(), "readTickerFromDateTo_EndOfTheYear", String.valueOf(dSList.size()));
                for (DocumentSnapshot ds : dSList) {

                    TickerDTO tickerDTO = ds.toObject(TickerDTO.class);
                    tickerList.add(tickerDTO);
                    H.debugLog(this.getClass().getSimpleName(), "readTickerFromDateTo_EndOfTheYear: " + tickerDTO.getSymbol() + tickerDTO.getCalendar());
                }
                /*-----------returning tickers-------------------------*/
                if (listener != null) {
                    listener.onSuccess(tickerList);
                }
            } else {
                H.errorLog(this.getClass().getSimpleName(), "readTickerFromDateTo_EndOfTheYear", task.getException().getLocalizedMessage());
            }
        });


    }

}
