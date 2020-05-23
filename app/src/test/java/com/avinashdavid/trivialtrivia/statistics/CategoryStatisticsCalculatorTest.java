package com.avinashdavid.trivialtrivia.statistics;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import com.avinashdavid.trivialtrivia.Utility;
import com.avinashdavid.trivialtrivia.data.QuizDBContract;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.fakes.RoboCursor;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@PrepareForTest(Utility.class) // allow mocking static methods in Utility class
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@RunWith(RobolectricTestRunner.class)
public class CategoryStatisticsCalculatorTest
{
    private static final double EPSILON = 0.00001;

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    Context mockContext;
    @Mock
    ContentResolver mockContentResolver;

    private static final String[] CATEGORY_COLUMNS = new String[]{
            QuizDBContract.CategoryEntry.COLUMN_NAME_NAME,
            QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_QUESTIONS_ANSWERED,
            QuizDBContract.CategoryEntry.COLUMN_NAME_CORRECTLY_ANSWERED,
            QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_TIME_OVERALL,
            QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_TIME_CORRECT,
            QuizDBContract.CategoryEntry.COLUMN_NAME_TOTAL_TIME_WRONG
    };

    private void setupContentResolver(RoboCursor cursor)
    {
        when(mockContentResolver.query(any(Uri.class), any(String[].class), (String) isNull(), (String[]) isNull(), (String) isNull()))
                .thenReturn(cursor);
        when(mockContentResolver.query(any(Uri.class), any(String[].class), (String) isNull(), (String[]) isNull(), anyString()))
                .thenReturn(cursor);
        when(mockContext.getApplicationContext()).thenReturn(mockContext);
        when(mockContext.getContentResolver()).thenReturn(mockContentResolver);
        if (cursor != null)
        {
            cursor.setColumnNames(new ArrayList<>(Arrays.asList(CATEGORY_COLUMNS)));
        }
    }

    @Test
    public void getCategoryPerformanceReportsReturnsSameResultAsCalculateIfCalculateCalled()
    {
        RoboCursor fakeCursor = new RoboCursor();
        setupContentResolver(fakeCursor);
        fakeCursor.setResults(new Object[][]
                {
                        new Object[]{99, 10, 6, 25, 10, 15}
                });

        ArrayList<double[]> expected = CategoryStatisticsCalculator.calculateCategoryPerformanceReports(mockContext, 1);
        ArrayList<double[]> actual = CategoryStatisticsCalculator.getCategoryPerformanceReports();
        assertEquals(expected, actual);
    }

    @Test
    public void getCategoryPerformanceReportsReturnsLatestResultIfCalculateCalledTwiceWithDifferentValues()
    {
        RoboCursor fakeCursor = new RoboCursor();
        setupContentResolver(fakeCursor);
        fakeCursor.setResults(new Object[][]
                {
                        new Object[]{99, 10, 6, 25, 10, 15}
                });

        ArrayList<double[]> firstCall = CategoryStatisticsCalculator.calculateCategoryPerformanceReports(mockContext, 2);

        fakeCursor.setResults(new Object[][]
                {
                        new Object[]{99, 10, 6, 25, 10, 15},
                        new Object[]{99, 15, 8, 50, 30, 20}
                });
        ArrayList<double[]> expected = CategoryStatisticsCalculator.calculateCategoryPerformanceReports(mockContext, 3);
        ArrayList<double[]> actual = CategoryStatisticsCalculator.getCategoryPerformanceReports();
        assertEquals(expected, actual);
        assertNotEquals(firstCall, actual);
    }


    @Test
    public void getCategoryPerformanceReportsReturnsSameResultIfCalculateCalledTwiceWithSameValues()
    {
        RoboCursor fakeCursor = new RoboCursor();
        setupContentResolver(fakeCursor);
        fakeCursor.setResults(new Object[][]
                {
                        new Object[]{99, 10, 6, 25, 10, 15}
                });

        ArrayList<double[]> firstCall = CategoryStatisticsCalculator.calculateCategoryPerformanceReports(mockContext, 10);
        ArrayList<double[]> expected = CategoryStatisticsCalculator.calculateCategoryPerformanceReports(mockContext, 10);
        ArrayList<double[]> actual = CategoryStatisticsCalculator.getCategoryPerformanceReports();
        assertEquals(expected, actual);
        assertEquals(firstCall, actual);
    }

    // Test fails due to code bug
    @Test
    public void calculateCategoryPerformanceReportsWithNullCursor()
    {
        setupContentResolver(null);
        ArrayList<double[]> actual = CategoryStatisticsCalculator.calculateCategoryPerformanceReports(mockContext, 4);
        assertNotNull(actual);
        assertEquals(actual.size(), 0);
    }

    // Test fails due to code bug
    @Test
    public void calculateCategoryPerformanceReportsWithoutRow()
    {
        RoboCursor fakeCursor = new RoboCursor();
        setupContentResolver(fakeCursor);
        fakeCursor.setResults(new Object[][]{});
        ArrayList<double[]> expected = new ArrayList<>(Arrays.asList(new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0}));
        ArrayList<double[]> actual = CategoryStatisticsCalculator.calculateCategoryPerformanceReports(mockContext, 5);
        assertNotNull("Actual should not be null", actual);
        assertNestedDoubleArraysAreEqual(expected, actual);
    }

    @Test
    public void calculateCategoryPerformanceReportsWithOneRow()
    {
        RoboCursor fakeCursor = new RoboCursor();
        setupContentResolver(fakeCursor);
        fakeCursor.setResults(new Object[][]
                {
                        new Object[]{99, 10, 6, 25, 10, 15}
                });

        ArrayList<double[]> expected = new ArrayList<>(Arrays.asList(new double[]{10.0, 6.0, 60.0, 2.5, 1.66666666, 3.75}));
        ArrayList<double[]> actual = CategoryStatisticsCalculator.calculateCategoryPerformanceReports(mockContext, 6);
        assertNestedDoubleArraysAreEqual(expected, actual);
    }

    @Test
    public void calculateCategoryPerformanceReportsWithZeroTotalAnswered()
    {
        RoboCursor fakeCursor = new RoboCursor();
        setupContentResolver(fakeCursor);
        fakeCursor.setResults(new Object[][]
                {
                        new Object[]{99, 0, 0, 50, 0, 0}
                });

        ArrayList<double[]> expected = new ArrayList<>(Arrays.asList(new double[]{0, 0, 0, 0, 0, 0}));
        ArrayList<double[]> actual = CategoryStatisticsCalculator.calculateCategoryPerformanceReports(mockContext, 7);
        assertNestedDoubleArraysAreEqual(expected, actual);
    }

    @Test
    public void calculateCategoryPerformanceReportsWithZeroCorrectAnswers()
    {
        RoboCursor fakeCursor = new RoboCursor();
        setupContentResolver(fakeCursor);
        fakeCursor.setResults(new Object[][]
                {
                        new Object[]{99, 2, 0, 50, 0, 50}
                });

        ArrayList<double[]> expected = new ArrayList<>(Arrays.asList(new double[]{2.0, 0, 0, 25.0, -1.0, 25.0}));
        ArrayList<double[]> actual = CategoryStatisticsCalculator.calculateCategoryPerformanceReports(mockContext, 8);
        assertNestedDoubleArraysAreEqual(expected, actual);
    }

    @Test
    public void calculateCategoryPerformanceReportsWithZeroWrongAnswers()
    {
        RoboCursor fakeCursor = new RoboCursor();
        setupContentResolver(fakeCursor);
        fakeCursor.setResults(new Object[][]
                {
                        new Object[]{99, 2, 2, 50, 50, 0}
                });

        ArrayList<double[]> expected = new ArrayList<>(Arrays.asList(new double[]{2.0, 2.0, 100.0, 25.0, 25.0, -1.0}));
        ArrayList<double[]> actual = CategoryStatisticsCalculator.calculateCategoryPerformanceReports(mockContext, 9);
        assertNestedDoubleArraysAreEqual(expected, actual);
    }

    private void assertNestedDoubleArraysAreEqual(ArrayList<double[]> expected, ArrayList<double[]> actual)
    {
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++)
        {
            assertArrayEquals(expected.get(i), actual.get(i), EPSILON);
        }
    }

    @Test
    public void getBestAndWorstCategoryStrings()
    {
        RoboCursor fakeCursor = new RoboCursor();
        setupContentResolver(fakeCursor);
        fakeCursor.setResults(new Object[][]
                {
                        new Object[]{1, 2, 1, 50, 25, 25},  // 50%
                        new Object[]{2, 2, 2, 50, 50, 0},  // 100%
                        new Object[]{3, 2, 0, 50, 0, 50},  // 0%
                        new Object[]{4, 2, 1, 50, 25, 25},  // 50%
                        new Object[]{5, 2, 0, 50, 0, 50},  // 0%
                        new Object[]{6, 2, 2, 50, 50, 0},  // 100%
                });

        ArrayList<String> expected = new ArrayList<>(Arrays.asList("2", "3, 5"));
        ArrayList<String> actual = CategoryStatisticsCalculator.getBestAndWorstCategoryStrings(mockContext);
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void getBestAndWorstCategoryPercentages()
    {
        RoboCursor fakeCursor = new RoboCursor();
        setupContentResolver(fakeCursor);
        fakeCursor.setResults(new Object[][]
                {
                        new Object[]{1, 2, 1, 50, 25, 25},  // 50%
                        new Object[]{2, 2, 2, 50, 50, 0},  // 100%
                        new Object[]{3, 2, 0, 50, 0, 50},  // 0%
                        new Object[]{4, 2, 1, 50, 25, 25},  // 50%
                        new Object[]{5, 2, 0, 50, 0, 50},  // 0%
                        new Object[]{6, 2, 2, 50, 50, 0},  // 100%
                });

        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(100, 0));
        ArrayList<Integer> actual = CategoryStatisticsCalculator.getBestAndWorstCategoryPercentages(mockContext);
        assertArrayEquals(expected.toArray(), actual.toArray());
    }
}