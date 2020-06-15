package org.jet.sql.codegen.wrapper;

import org.jet.sql.codegen.wrapper.model.QueryArgument;
import org.jet.sql.codegen.wrapper.model.ResultColumns;
import org.jet.sql.codegen.wrapper.model.SqlQuery;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author tgorthi
 * @since Jun 2020
 */
public class QueryParser
{
    private static final String QUERY_PARAM_PREFIX = "arg_";
    private static final Pattern QUERY_PARAM_PATTERN = Pattern.compile(QUERY_PARAM_PREFIX + "([a-zA-Z_0-9]*)");

    public static SqlQuery convert(final String name, final String sql, final Connection connection) throws SQLException
    {
        String formattedSql = sql;
        final Matcher matcher = QUERY_PARAM_PATTERN.matcher(formattedSql);

        final List<String> argumentNames = new ArrayList<>();
        while (matcher.find())
        {
            final String parsedQueryParam = matcher.group(1);
            formattedSql = formattedSql.replaceFirst(QUERY_PARAM_PREFIX + parsedQueryParam, "?");

            argumentNames.add(parsedQueryParam);
        }

        final PreparedStatement ps = connection.prepareStatement(formattedSql);

        return new SqlQuery(name, formattedSql, _getQueryArguments(ps, argumentNames), _getResultColumns(ps));
    }

    private static QueryArgument[] _getQueryArguments(final PreparedStatement ps, final List<String> argumentNames) throws SQLException
    {
        final ParameterMetaData parameterMetaData = ps.getParameterMetaData();

        if (parameterMetaData != null)
        {
            final int parameterCount = parameterMetaData.getParameterCount();
            final QueryArgument[] queryArguments = new QueryArgument[parameterCount];

            for (int i = 1; i <= parameterCount; i++)
            {
                queryArguments[i - 1] = new QueryArgument(argumentNames.get(i - 1),
                        parameterMetaData.getParameterType(i), parameterMetaData.getParameterClassName(i), i);
            }

            return queryArguments;
        }

        return null;
    }

    private static ResultColumns[] _getResultColumns(final PreparedStatement ps) throws SQLException
    {
        final ResultSetMetaData resultSetMetaData = ps.getMetaData();

        if (resultSetMetaData != null)
        {
            final int resultSetColumnCount = resultSetMetaData.getColumnCount();
            final ResultColumns[] resultColumns = new ResultColumns[resultSetColumnCount];

            for (int i = 1; i <= resultSetColumnCount; i++)
            {
                resultColumns[i - 1] = new ResultColumns(resultSetMetaData.getColumnName(i),
                        resultSetMetaData.getColumnClassName(i));
            }

            return resultColumns;
        }


        return null;
    }
}
