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
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.fakes.RoboCursor;

import java.util.ArrayList;
import java.util.Arrays;

import static com.avinashdavid.trivialtrivia.statistics.OverallStatisticsCalculator.AVERAGE_PERCENTAGE_SCORE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@PrepareForTest(Utility.class) // allow mocking static methods in Utility class
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@RunWith(RobolectricTestRunner.class)
public class OverallStatisticsCalculatorTest
{
    private static final double EPSILON = 0.0001;

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    // This test fails due to code bug
    @Test(expected = IllegalArgumentException.class)
    public void getNumberOfPerfectQuizzesWithNullScoreListThrows()
    {
        int[] quizsizelist = new int[]{2};
        OverallStatisticsCalculator.getNumberOfPerfectQuizzes(null, quizsizelist);
    }

    // This test fails due to code bug
    @Test(expected = IllegalArgumentException.class)
    public void getNumberOfPerfectQuizzesWithNullQuizSizeListThrows()
    {
        int[] scorelist = new int[]{7, 3};
        OverallStatisticsCalculator.getNumberOfPerfectQuizzes(scorelist, null);
    }

    @Test
    public void getNumberOfPerfectQuizzesWithNotEqualInputLists()
    {
        int[] scorelist = new int[]{3, 7};
        int[] quizsizelist = new int[]{2};
        double actual = OverallStatisticsCalculator.getNumberOfPerfectQuizzes(scorelist, quizsizelist);
        double expected = -1;
        assertEquals(expected, actual, EPSILON);
    }


    @Test
    public void getNumberOfPerfectQuizzesWithEqualInputLists()
    {
        int[] scorelist = new int[]{3, 7, 10};
        int[] quizsizelist = new int[]{2, 7, 3};
        double actual = OverallStatisticsCalculator.getNumberOfPerfectQuizzes(scorelist, quizsizelist);
        double expected = 1;
        assertEquals(expected, actual, EPSILON);
    }

    @Mock
    Context mockContext;
    @Mock
    ContentResolver mockContentResolver;

    private static final String[] CALCULATOR_COLUMNS = new String[]{
            QuizDBContract.QuizEntry.COLUMN_NAME_QUIZ_SIZE,
            QuizDBContract.QuizEntry.COLUMN_NAME_SCORE,
            QuizDBContract.QuizEntry.COLUMN_NAME_AVERAGE_TIME_OVERALL,
            QuizDBContract.QuizEntry.COLUMN_NAME_AVERAGE_TIME_CORRECT,
            QuizDBContract.QuizEntry.COLUMN_NAME_AVERAGE_TIME_WRONG
    };

    private void setupContentResolver(RoboCursor cursor)
    {
        when(mockContentResolver.query(any(Uri.class), any(String[].class), (String) isNull(), (String[]) isNull(), (String) isNull()))
                .thenReturn(cursor);
        when(mockContext.getContentResolver()).thenReturn(mockContentResolver);
        if (cursor != null)
        {
            cursor.setColumnNames(new ArrayList<>(Arrays.asList(CALCULATOR_COLUMNS)));
        }
    }

    // This test fails due to code bug
    @Test(expected = IllegalArgumentException.class)
    public void getOverallPerformanceAndAveragesWithNullInput()
    {
        OverallStatisticsCalculator.getOverallPerformanceAndAverages(null);
    }

    // This test fails due to code bug
    @Test
    public void getOverallPerformanceAndAveragesWithNullCursor()
    {
        setupContentResolver(null);
        ArrayList<Double> actual = OverallStatisticsCalculator.getOverallPerformanceAndAverages(mockContext);
        assertNotNull(actual);
        assertEquals(0, actual.size());
    }

    // This test fails due to code bug
    @Test
    public void getOverallPerformanceAndAveragesWithoutRow()
    {
        RoboCursor fakeCursor = new RoboCursor();
        setupContentResolver(fakeCursor);
        fakeCursor.setResults(new Object[][]{});
        ArrayList<Double> expected = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
        ArrayList<Double> actual = OverallStatisticsCalculator.getOverallPerformanceAndAverages(mockContext);
        assertEquals(expected, actual);
    }

    @Test
    public void getOverallPerformanceAndAveragesWithOneRow()
    {
        RoboCursor fakeCursor = new RoboCursor();
        setupContentResolver(fakeCursor);
        fakeCursor.setResults(
                new Object[][]
                        {
                                new Object[]{5, 5, 7.0, 8.0, 9.0}
                        }
        );
        ArrayList<Double> expected = new ArrayList<>(Arrays.asList(1.0, 5.0, 1.0, 100.0, 8.0, 9.0, 1.0));
        ArrayList<Double> actual = OverallStatisticsCalculator.getOverallPerformanceAndAverages(mockContext);
        assertEquals(expected, actual);
    }

    @Test
    public void getOverallPerformanceAndAveragesWithTwoRows()
    {
        RoboCursor fakeCursor = new RoboCursor();
        setupContentResolver(fakeCursor);
        fakeCursor.setResults(
                new Object[][]
                        {
                                new Object[]{5, 6, 7.0, 8.0, 9.0},
                                new Object[]{5, 6, 7.0, 8.0, 9.0},
                        }
        );
        ArrayList<Double> expected = new ArrayList<>(Arrays.asList(2.0, 6.0, 0.0, 120.0, 8.0, 9.0, 2.0));
        ArrayList<Double> actual = OverallStatisticsCalculator.getOverallPerformanceAndAverages(mockContext);
        assertEquals(expected, actual);
    }

    // This test fails due to code bug
    @Test
    public void getOverallPerformanceAndAveragesWhenAveragePercentageScoreThrows() throws Exception
    {
        RoboCursor fakeCursor = new RoboCursor();
        setupContentResolver(fakeCursor);
        fakeCursor.setResults(
                new Object[][]
                        {
                                new Object[]{5, 5, 7.0, 8.0, 9.0}
                        }
        );

        PowerMockito.spy(Utility.class); // Partial mock for Utility class
        PowerMockito.doThrow(new Exception()).when(Utility.class, "getAveragePercentageFromListsOfActualAndTotal", any(int[].class), any(int[].class));
        ArrayList<Double> actual = OverallStatisticsCalculator.getOverallPerformanceAndAverages(mockContext);
        assertEquals(0.0, actual.get(AVERAGE_PERCENTAGE_SCORE), EPSILON);
    }
}