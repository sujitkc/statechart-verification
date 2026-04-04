#!/usr/bin/env python3
"""
Generator script for Concurrent Sieve of Eratosthenes ConStaBL statecharts.
Generates a shell-state based concurrent sieve with one region per prime filter
plus a collector region.

Usage: python3 gen_sieve.py <MAX> > sieve_concurrent_<MAX>.stb
"""
import math
import sys

def generate_sieve_stb(MAX):
    # Compute sieve primes (p where p^2 <= MAX)
    sieve_primes = []
    for p in range(2, int(math.isqrt(MAX)) + 1):
        is_prime = True
        for d in range(2, int(math.isqrt(p)) + 1):
            if p % d == 0:
                is_prime = False
                break
        if is_prime:
            sieve_primes.append(p)

    # Compute actual primes for comments
    primes_list = []
    for n in range(2, MAX+1):
        is_p = True
        for d in range(2, int(math.isqrt(n)) + 1):
            if n % d == 0:
                is_p = False
                break
        if is_p:
            primes_list.append(n)

    SC = f"ConcurrentSieve" if MAX == 25 else f"ConcurrentSieve{MAX}"
    
    lines = []
    def L(s=""): lines.append(s)
    
    L(f"// Concurrent Sieve of Eratosthenes - finds all prime numbers up to {MAX}")
    L(f"// Architecture: {len(sieve_primes)} concurrent sieve filters + 1 collector running in parallel")
    L(f"// Sieve filters for primes: {', '.join(str(p) for p in sieve_primes)}")
    L(f"// Expected result: {len(primes_list)} primes")
    primes_str = ', '.join(str(p) for p in primes_list)
    L(f"// Primes: {primes_str}")
    L()
    L(f"statechart {SC} {{")
    L()
    L(f"  events {{")
    L(f"    step;")
    L(f"  }}")
    L()
    
    # Variable declarations
    L(f"  // Primality flags: 1 = prime candidate, 0 = composite")
    for n in range(2, MAX+1):
        L(f"  p{n} : int : local;")
    L()
    L(f"  // Working variables for each concurrent sieve")
    for p in sieve_primes:
        L(f"  mult{p} : int : local;")
    L()
    L(f"  // Result collection")
    L(f"  primeCount : int : local;")
    L(f"  candidate  : int : local;")
    L()
    
    # Entry block
    L(f"  entry : {{")
    for n in range(2, MAX+1):
        L(f"    p{n} := 1;")
    for p in sieve_primes:
        L(f"    mult{p} := {p*p};")
    L(f"    primeCount := 0;")
    L(f"    candidate := 2;")
    L(f"  }}")
    L()
    L(f"  exit : {{}}")
    L()
    L(f"  functions {{")
    L(f"    input<||>() : int;")
    L(f"  }}")
    L()
    L(f"  state Init {{")
    L(f"    entry : {{}}")
    L(f"    exit  : {{}}")
    L(f"  }}")
    L()
    
    # Shell state
    L(f"  // Shell state with {len(sieve_primes) + 1} concurrent regions ({len(sieve_primes)} sieves + 1 collector)")
    L(f"  shell SievePipeline {{")
    L(f"    entry : {{}}")
    L(f"    exit  : {{}}")
    
    # Generate each sieve region
    for p in sieve_primes:
        multiples = list(range(p*p, MAX+1, p))
        prefix = f"S{p}"
        L()
        L(f"    // Region: Sieve{p} - concurrently marks multiples of {p}")
        L(f"    // Marks {len(multiples)} multiples starting from {p}^2 = {p*p}")
        L(f"    state Sieve{p} {{")
        L(f"      entry : {{}}")
        L(f"      exit  : {{}}")
        L()
        L(f"      state {prefix}_MarkA {{ entry : {{}} exit : {{}} }}")
        L(f"      state {prefix}_MarkB {{ entry : {{}} exit : {{}} }}")
        L(f"      state {prefix}_Done  {{ entry : {{}} exit : {{}} }}")
        
        # Generate transitions alternating A->B->A for each multiple
        cur_state = "A"
        for m in multiples:
            nxt_state = "B" if cur_state == "A" else "A"
            L()
            L(f"      transition t_s{p}_m{cur_state}{nxt_state}_{m} {{")
            L(f"        source      : {SC}.SievePipeline.Sieve{p}.{prefix}_Mark{cur_state};")
            L(f"        destination : {SC}.SievePipeline.Sieve{p}.{prefix}_Mark{nxt_state};")
            L(f"        trigger     : step;")
            L(f"        guard       : (mult{p} = {m});")
            L(f"        action      : {{ p{m} := 0; mult{p} := mult{p} + {p}; }}")
            L(f"      }}")
            cur_state = nxt_state
        
        # Done transitions from whichever state we ended on, plus the other
        for st in ["A", "B"]:
            L()
            L(f"      transition t_s{p}_m{st}_done {{")
            L(f"        source      : {SC}.SievePipeline.Sieve{p}.{prefix}_Mark{st};")
            L(f"        destination : {SC}.SievePipeline.Sieve{p}.{prefix}_Done;")
            L(f"        trigger     : step;")
            L(f"        guard       : (mult{p} > {MAX});")
            L(f"        action      : {{}}")
            L(f"      }}")
        
        L(f"    }}")
    
    # Collector region
    L()
    L(f"    // Region: Collector - waits for all sieves to finish, then counts primes")
    L(f"    state Collector {{")
    L(f"      entry : {{}}")
    L(f"      exit  : {{}}")
    L()
    L(f"      state CollWaiting {{ entry : {{}} exit : {{}} }}")
    L(f"      state CollCountA  {{ entry : {{}} exit : {{}} }}")
    L(f"      state CollCountB  {{ entry : {{}} exit : {{}} }}")
    L(f"      state CollDone    {{ entry : {{}} exit : {{}} }}")
    L()
    
    # Wait transition
    guard_parts = " && ".join(f"(mult{p} > {MAX})" for p in sieve_primes)
    L(f"      // Wait for all sieves to complete")
    L(f"      transition t_coll_start {{")
    L(f"        source      : {SC}.SievePipeline.Collector.CollWaiting;")
    L(f"        destination : {SC}.SievePipeline.Collector.CollCountA;")
    L(f"        trigger     : step;")
    L(f"        guard       : {guard_parts};")
    L(f"        action      : {{ candidate := 2; primeCount := 0; }}")
    L(f"      }}")
    
    # Count transitions alternating A->B->A
    cur_state = "A"
    for n in range(2, MAX+1):
        nxt_state = "B" if cur_state == "A" else "A"
        L()
        L(f"      transition t_coll_c{cur_state}{nxt_state}_{n} {{")
        L(f"        source      : {SC}.SievePipeline.Collector.CollCount{cur_state};")
        L(f"        destination : {SC}.SievePipeline.Collector.CollCount{nxt_state};")
        L(f"        trigger     : step;")
        L(f"        guard       : (candidate = {n});")
        L(f"        action      : {{ primeCount := primeCount + p{n}; candidate := {n+1}; }}")
        L(f"      }}")
        cur_state = nxt_state
    
    # Done transitions from both states
    for st in ["A", "B"]:
        L()
        L(f"      transition t_coll_c{st}_done {{")
        L(f"        source      : {SC}.SievePipeline.Collector.CollCount{st};")
        L(f"        destination : {SC}.SievePipeline.Collector.CollDone;")
        L(f"        trigger     : step;")
        L(f"        guard       : (candidate > {MAX});")
        L(f"        action      : {{}}")
        L(f"      }}")
    
    L(f"    }}")
    L(f"  }}")
    L()
    L(f"  state Done {{")
    L(f"    entry : {{}}")
    L(f"    exit  : {{}}")
    L(f"  }}")
    L()
    L(f"  // Transition from Init to concurrent pipeline")
    L(f"  transition t_start {{")
    L(f"    source      : {SC}.Init;")
    L(f"    destination : {SC}.SievePipeline;")
    L(f"    trigger     : step;")
    L(f"    guard       : true;")
    L(f"    action      : {{}}")
    L(f"  }}")
    L(f"}}")
    
    return "\n".join(lines)

if __name__ == "__main__":
    MAX = int(sys.argv[1]) if len(sys.argv) > 1 else 25
    print(generate_sieve_stb(MAX))
