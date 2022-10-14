#include "header.hh"
#include <klee/klee.h>
bool ZPyWpd;
int eVDzVf;
int JNkzvx;
int JZdCDT;
int state;
int event;
int FKypcC;
int ktqtTb;
void run(int *events, int N) {
  FKypcC = 0;
  ktqtTb = 0;
  state = FKypcC;
  for (int i = 0; i < N; i++) {
    event = events[i];
    if ((state == FKypcC)) {
    } else {
    }
  }
  return;
}
int main() {
  int N = 5;
  int events[N];
  klee_make_symbolic(events, sizeof events, "events");
  run(events, N);
  return 0;
}
