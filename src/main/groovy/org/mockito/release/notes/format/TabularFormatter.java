package org.mockito.release.notes.format;

import org.mockito.release.notes.model.ReleaseNotesData;
import org.mockito.release.notes.model.ReleaseNotesFormat;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Tabular formatter
 *
 * IN PROGRESS
 */
public class TabularFormatter implements ReleaseNotesFormatter {

    @Override
    public String formatNotes(ReleaseNotesData data, ReleaseNotesFormat format) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm z");
        f.setTimeZone(TimeZone.getTimeZone("UTC"));
        String now = f.format(data.getDate());

        return "### " + data.getVersion() + " (" + now + ")";
    }
}