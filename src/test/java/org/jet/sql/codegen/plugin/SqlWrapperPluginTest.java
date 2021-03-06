package org.jet.sql.codegen.plugin;

import org.gradle.api.Project;
import org.testng.Assert;
import org.gradle.testfixtures.ProjectBuilder;
import org.testng.annotations.Test;

/**
 * Unit tests for {@link SqlWrapperPlugin}
 *
 * @author tgorthi
 * @since December 2019
 */
public class SqlWrapperPluginTest
{
    @Test
    public void testPluginIsApplied()
    {
        Project project = ProjectBuilder.builder().build();
        project.getPluginManager().apply(SqlWrapperPlugin.ID);

        Assert.assertTrue(project.getPluginManager().hasPlugin(SqlWrapperPlugin.ID));

        Assert.assertNotNull(project.getTasks().getByName(SqlWrapperPlugin.TASK_ID));
    }

}
