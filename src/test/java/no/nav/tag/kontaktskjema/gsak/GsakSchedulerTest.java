package no.nav.tag.kontaktskjema.gsak;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.LockingTaskExecutor;

public class GsakSchedulerTest {

    @Test
    public void skalSjekkeAtSceduledMetodeBrukerShedlock() {
        GsakScheduler gsakScheduler = new GsakScheduler();
        gsakScheduler.taskExecutor = mock(LockingTaskExecutor.class);
        
        gsakScheduler.scheduledOpprettOppgaveForSkjemaer();
        
        verify(gsakScheduler.taskExecutor).executeWithLock(any(Runnable.class), any(LockConfiguration.class));
    }

}
