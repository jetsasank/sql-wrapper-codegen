package org.jet.sql.codegen.wrapper.model;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.JDBCType;

/**
 * Unit tests for {@link ResultColumns}
 *
 * @author tgorthi
 * @since December 2019
 */
public class ResultColumnsTest
{
    @Test
    public void testGetBindVariableSetterMethodName()
    {
        final ResultColumns columnConfig = new ResultColumns("col_name", "varchar");
        Assert.assertEquals(columnConfig.evaluateAndGetResultSetAccessorMethodName(), "colName");
    }


    @Test(expectedExceptions = RuntimeException.class)
    public void testGetBindVariableSetterMethodNameWithSpecialCharacters()
    {
        final ResultColumns columnConfig = new ResultColumns("col_name$%", "varchar");
        columnConfig.evaluateAndGetResultSetAccessorMethodName();
    }

    @Test
    public void testGetBindVariableSetterMethodNameWithNumericCharacters()
    {
        final ResultColumns columnConfig = new ResultColumns("col_name_1", "varchar");
        Assert.assertEquals(columnConfig.evaluateAndGetResultSetAccessorMethodName(), "colName1");
    }
}