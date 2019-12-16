package ru.reliableteam.noteorganizer.notes.single_note_activity.presenter;

import android.graphics.Typeface;
import android.text.Editable;
import android.text.ParcelableSpan;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

import java.util.ArrayList;
import java.util.List;

import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.notes.model.Span;
import ru.reliableteam.noteorganizer.notes.single_note_activity.StyleState;
import ru.reliableteam.noteorganizer.notes.single_note_activity.view.SingleNoteActivity;

public class TextSpanPresenter implements StyleState {
    private SingleNoteActivity view;
    private int styleState = StyleState.REGULAR;

    public TextSpanPresenter (SingleNoteActivity activity) {
        this.view = activity;
    }

    public Spannable setTextStyle(Spannable sb, int startSelection, int endSelection, int noteTextLength) {
        List<Span> spanList = new ArrayList<>();

        if (endSelection != startSelection) {

            StyleSpan[] ss = sb.getSpans(0, noteTextLength, StyleSpan.class);

            // clear text span
            for (StyleSpan s : ss) {
                Span span = new Span(sb.getSpanStart(s), sb.getSpanEnd(s), s.getStyle());
                spanList.add(span);
                sb.removeSpan(s);
            }

            sb.setSpan(getStyle(styleState), startSelection, endSelection, Spanned.SPAN_MARK_MARK);// Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

            // recover span
            for (Span span : spanList)
                if (span.start != startSelection && span.end != endSelection) {
                    sb.setSpan(getStyle(span.style), span.start, span.end, Spanned.SPAN_MARK_MARK);//Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                }
        }
        return sb;
    }

    private void changeStyleState(int newStyleState) {
        styleState = newStyleState;
    }

    public void setStyle(boolean isChecked, int state) {
        if (!isChecked && styleState == state)
            changeStyleState(StyleState.REGULAR);
        if (isChecked)
            changeStyleState(state);
    }
    public ParcelableSpan getStyle() {
        System.out.println("get style() " + styleState);
        ParcelableSpan span = null;
        if (styleState == StyleState.UNDERLINE)
            span = new UnderlineSpan();
        else if (styleState == StyleState.STRIKE)
            span = new StrikethroughSpan();
        else
            span = new StyleSpan(styleState);

        return span;
    }
    private ParcelableSpan getStyle(int styleState) {
        ParcelableSpan span = null;
        if (styleState == StyleState.UNDERLINE)
            span = new UnderlineSpan();
        else if (styleState == StyleState.STRIKE)
            span = new StrikethroughSpan();
        else
            span = new StyleSpan(styleState);

        return span;
    }

    public void changeStyleStateToDefault() {
        styleState = StyleState.REGULAR;
    }

    public void onTextSelectionChanged(Spannable sb, int startSelection, Editable text) {
        StyleSpan[] ss = null;

        if (startSelection < 1)
            return;

        if (text.charAt(startSelection - 1) == ' ')
            ss = sb.getSpans(startSelection, startSelection + 1, StyleSpan.class);
        else
            ss = sb.getSpans(startSelection - 1, startSelection, StyleSpan.class);
        if (ss.length > 0) {
            for (StyleSpan s : ss) {
                System.out.println(s.getStyle());
                changeStyleState(s.getStyle());
            }
        } else {
            changeStyleState(StyleState.REGULAR);
        }

        setCheckedButtons();
    }

    private void setCheckedButtons() {
        System.out.println("prev style = " + styleState);
        switch (styleState) {
            case StyleState.BOLD:
                view.setCheckedButtons(R.id.bold_style);
                break;
            case StyleState.ITALIC:
                view.setCheckedButtons(R.id.italic_style);
                break;
            case StyleState.UNDERLINE:
                view.setCheckedButtons(R.id.underline_style);
                break;
            case StyleState.STRIKE:
                view.setCheckedButtons(R.id.strike_style);
                break;
            default:
                view.clearCheckedButtons();
                styleState = StyleState.REGULAR;
                break;
        }
        System.out.println("new style = " + styleState);
    }
}
