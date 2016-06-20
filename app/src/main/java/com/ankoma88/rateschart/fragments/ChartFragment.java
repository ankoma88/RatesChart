package com.ankoma88.rateschart.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidplot.Plot;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.ankoma88.rateschart.R;
import com.ankoma88.rateschart.interfaces.DataLoadListener;
import com.ankoma88.rateschart.model.Rate;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ankoma88 on 18.06.16.
 */
public class ChartFragment extends Fragment {
    private static final String TAG = ChartFragment.class.getSimpleName();

    private DataLoadListener dataLoadListener;
    private LinkedList<Number> time = new LinkedList<>();
    private LinkedList<Number> prices = new LinkedList<>();
    private LineAndPointFormatter formatter;

    @Bind(R.id.plot)
    XYPlot plot;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataLoadListener = (DataLoadListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dataLoadListener = null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataLoadListener.loadData();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chart, container, false);
        ButterKnife.bind(this, rootView);
        configureChart();
        return rootView;
    }

    private void updateChart(XYSeries series) {
        plot.clear();
        plot.addSeries(formatter, series);
        plot.redraw();
    }

    private void configureChart() {
        plot.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);
        plot.getGraphWidget().getDomainGridLinePaint().setColor(Color.BLACK);
        plot.getGraphWidget().getDomainGridLinePaint().setPathEffect(new DashPathEffect(new float[]{1, 1}, 1));
        plot.getGraphWidget().getDomainOriginLinePaint().setColor(Color.BLACK);
        plot.getGraphWidget().getRangeOriginLinePaint().setColor(Color.BLACK);

        plot.setBorderStyle(Plot.BorderStyle.SQUARE, null, null);
        plot.getBorderPaint().setStrokeWidth(1);
        plot.getBorderPaint().setAntiAlias(false);
        plot.getBorderPaint().setColor(Color.WHITE);

        Paint lineFill = new Paint();
        lineFill.setAlpha(200);
        lineFill.setShader(new LinearGradient(0, 0, 0, 250, Color.YELLOW, Color.BLUE, Shader.TileMode.MIRROR));

        formatter = new LineAndPointFormatter(Color.RED, Color.BLACK, Color.BLUE, null);
        formatter.setFillPaint(lineFill);
        plot.getGraphWidget().setPaddingRight(2);
        plot.getGraphWidget().setDomainLabelOrientation(-45);
        plot.getGraphWidget().getDomainCursorPaint().setTextAlign(Paint.Align.RIGHT);

        plot.setDomainLabel("Time");
        plot.setRangeLabel("Price");
        plot.setRangeValueFormat(new DecimalFormat("#.##"));
        plot.setDomainValueFormat(new Format() {

            @SuppressLint("SimpleDateFormat")
            private SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");

            @Override
            public StringBuffer format(Object obj, @NonNull StringBuffer toAppendTo, @NonNull FieldPosition pos) {

                long timestamp = ((Number) obj).longValue();
                Date date = new Date(timestamp);
                return dateFormat.format(date, toAppendTo, pos);
            }

            @Override
            public Object parseObject(String source, @NonNull ParsePosition pos) {
                return null;

            }
        });

    }

    public void onDataLoaded(Rate rate) {
        Log.d(TAG, "onDataLoaded: " + rate);
        time.add(rate.getTime());
        prices.add(rate.getPrice());
        XYSeries series = new SimpleXYSeries(time, prices, rate.getAsset());
        updateChart(series);
    }
}
