package com.avinashdavid.trivialtrivia.acknowledgments;

import android.net.Uri;

/**
 * Created by avinashdavid on 12/5/16.
 */

public class AcknowledgmentObject {
    private String name;
    private Uri link;

    public AcknowledgmentObject(String name, Uri link) {
        this.name = name;
        this.link = link;
    }

    public AcknowledgmentObject(String name, String link){
        this.name = name;
        this.link = Uri.parse(link);
    }

    public String getName() {
        return name;
    }

    public Uri getLink() {
        return link;
    }
}
