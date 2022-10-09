#include <assert.h>

void dump_events (int * events, int N) {
	for (int i = 0; i < N; i++) {
		printf ("%s\n", reverse_event_names[events[i]]);
	}
}

void stuck_spec (bool b) {
#ifdef STUCK_SPEC
	assert(b);
#endif
}
