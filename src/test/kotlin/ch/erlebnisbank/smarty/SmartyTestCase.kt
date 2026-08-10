package ch.erlebnisbank.smarty

import com.intellij.testFramework.LoggedErrorProcessor
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.util.ThrowableRunnable

/**
 * Base class for the Smarty tests.
 *
 * The tests run against IntelliJ IDEA Ultimate with every bundled plugin loaded, and a few of
 * those fail to initialise headlessly - Vue's LSP activation rule is one. The platform test
 * logger turns any logged error into a test failure, so such an error fails a Smarty test that
 * never touched the plugin in question. Only those are ignored here; everything else still
 * fails the test.
 */
abstract class SmartyTestCase : BasePlatformTestCase() {

    override fun runTestRunnable(testRunnable: ThrowableRunnable<Throwable>) {
        LoggedErrorProcessor.executeWith<Throwable>(IgnoreUnrelatedPlugins) {
            super.runTestRunnable(testRunnable)
        }
    }

    /**
     * Tearing the source root down deletes the temporary directories, and the resulting VFS
     * events are what trip the unrelated plugins - so this needs the same guard as the test
     * body itself.
     */
    override fun tearDown() {
        LoggedErrorProcessor.executeWith<Throwable>(IgnoreUnrelatedPlugins) {
            super.tearDown()
        }
    }

    private object IgnoreUnrelatedPlugins : LoggedErrorProcessor() {
        private val UNRELATED = listOf("vuejs", "VueLspServerActivationRule")

        override fun processError(
            category: String,
            message: String,
            details: Array<out String>,
            throwable: Throwable?
        ): Set<Action> {
            val text = message + throwable?.stackTraceToString().orEmpty()
            return if (UNRELATED.any { it in text }) Action.NONE else Action.ALL
        }
    }
}
