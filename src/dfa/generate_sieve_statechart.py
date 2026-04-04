#!/usr/bin/env python3
"""
Generate Concurrent Sieve of Eratosthenes ConStaBL Statechart

This script generates a ConStaBL statechart that implements the Sieve of Eratosthenes
algorithm using concurrent filters. Each sieve filter independently processes ALL
multiples of its prime (not just from p²), and uses guards to check whether each
multiple has already been marked by another filter.

Architecture:
  - N-1 concurrent sieve filters (one per prime up to sqrt(N))
  - 1 collector region that counts the primes after all filters complete
  - All regions run in parallel within a shell

Guard-based filtering:
  - Each filter transitions on ALL its multiples (from 2p to N)
  - For each multiple m: 
    * Mark transition: (mult = m) && (p_m = 1) -> set p_m := 0
    * Skip transition: (mult = m) && (p_m = 0) -> skip (already marked)
  - This allows faithful concurrent execution and accurate sieve behavior

Usage:
  python3 generate_sieve_statechart.py [N] [output_file]

  N: Upper bound (default: 49)
  output_file: Output .stb file (default: data/sieve_concurrent_N.stb)

Examples:
  python3 generate_sieve_statechart.py
  python3 generate_sieve_statechart.py 100
  python3 generate_sieve_statechart.py 25 data/sieve_custom.stb
"""

import sys
import os
from pathlib import Path


def get_primes_up_to(n):
    """Return list of primes up to n using trial division."""
    if n < 2:
        return []
    primes = [2]
    for i in range(3, n + 1, 2):
        is_prime = True
        for p in primes:
            if p * p > i:
                break
            if i % p == 0:
                is_prime = False
                break
        if is_prime:
            primes.append(i)
    return primes


def generate_sieve_statechart(n, output_file):
    """Generate ConStaBL sieve statechart for finding primes up to n."""
    
    sc = 'ConcurrentSieve' + str(n)
    lines = []
    
    def a(s):
        lines.append(s)
    
    # Header with documentation
    a('// =============================================================================')
    a(f'// Concurrent Sieve of Eratosthenes -- finds all prime numbers up to {n}')
    a('// =============================================================================')
    a('//')
    a('// Architecture: concurrent sieve filters + 1 collector running in parallel')
    a('// Sieve filters for each prime p: mark ALL multiples (from 2p to N)')
    a('// Guards check whether a number has already been marked by another filter;')
    a('// if already filtered (pN = 0), the mark transition is not taken and a')
    a('// skip transition advances the counter instead.')
    a('//')
    a('// This models the true concurrent sieve algorithm: every filter independently')
    a('// processes every multiple, relying only on the shared primality flags.')
    a('//')
    
    # Calculate actual primes up to n
    primes = get_primes_up_to(n)
    sieve_primes = [p for p in primes if p * p <= n]  # sieves are for primes up to sqrt(n)
    all_primes_count = len(primes)
    
    a(f'// Expected result: {all_primes_count} primes')
    a(f'// Primes: {", ".join(str(p) for p in primes)}')
    a('')
    a(f'statechart {sc} {{')
    a('')
    a('  events {')
    a('    step;')
    a('  }')
    a('')
    a('  // Primality flags: 1 = prime candidate, 0 = composite')
    for num in range(2, n + 1):
        a(f'  p{num} : int : local;')
    a('')
    a('  // Working variables for each concurrent sieve')
    for p in sieve_primes:
        a(f'  mult{p} : int : local;')
    a('')
    a('  // Result collection')
    a('  primeCount : int : local;')
    a('  candidate  : int : local;')
    a('')
    a('  entry : {')
    for num in range(2, n + 1):
        a(f'    p{num} := 1;')
    for p in sieve_primes:
        start = 2 * p
        a(f'    mult{p} := {start};')
    a('    primeCount := 0;')
    a('    candidate := 2;')
    a('  }')
    a('')
    a('  exit : {}')
    a('')
    a('  functions {')
    a('    input<||>() : int;')
    a('  }')
    a('')
    a('  state Init {')
    a('    entry : {}')
    a('    exit  : {}')
    a('  }')
    a('')
    a('  // Shell state with concurrent regions (sieves + collector)')
    a('  shell SievePipeline {')
    a('    entry : {}')
    a('    exit  : {}')
    a('')
    
    # Generate sieve regions
    for p in sieve_primes:
        start = 2 * p
        mults = list(range(start, n + 1, p))
        region = f'Sieve{p}'
        pfx = f'S{p}'
        mvar = f'mult{p}'
        
        a(f'    // Region: {region} -- marks ALL multiples of {p} (from {start} to {n})')
        a(f'    // Multiples: {", ".join(str(m) for m in mults)}')
        a(f'    // Guard checks: if p_N=1 (not yet marked) -> mark; if p_N=0 (already marked) -> skip')
        a(f'    state {region} {{')
        a(f'      entry : {{}}')
        a(f'      exit  : {{}}')
        a(f'')
        a(f'      state {pfx}_MarkA {{ entry : {{}} exit : {{}} }}')
        a(f'      state {pfx}_MarkB {{ entry : {{}} exit : {{}} }}')
        a(f'      state {pfx}_Done  {{ entry : {{}} exit : {{}} }}')
        a(f'')
        
        for i, mult in enumerate(mults):
            src = 'A' if i % 2 == 0 else 'B'
            dst = 'B' if i % 2 == 0 else 'A'
            d = f'{src}{dst}'
            
            # Mark transition: p_mult = 1 (not yet filtered)
            a(f'      transition t_{pfx.lower()}_mark_{d}_{mult} {{')
            a(f'        source      : {sc}.SievePipeline.{region}.{pfx}_Mark{src};')
            a(f'        destination : {sc}.SievePipeline.{region}.{pfx}_Mark{dst};')
            a(f'        trigger     : step;')
            a(f'        guard       : ({mvar} = {mult}) && (p{mult} = 1);')
            a(f'        action      : {{ p{mult} := 0; {mvar} := {mvar} + {p}; }}')
            a(f'      }}')
            
            # Skip transition: p_mult = 0 (already filtered by another sieve)
            a(f'      transition t_{pfx.lower()}_skip_{d}_{mult} {{')
            a(f'        source      : {sc}.SievePipeline.{region}.{pfx}_Mark{src};')
            a(f'        destination : {sc}.SievePipeline.{region}.{pfx}_Mark{dst};')
            a(f'        trigger     : step;')
            a(f'        guard       : ({mvar} = {mult}) && (p{mult} = 0);')
            a(f'        action      : {{ {mvar} := {mvar} + {p}; }}')
            a(f'      }}')
            a(f'')
        
        # Done transitions
        a(f'      transition t_{pfx.lower()}_mA_done {{')
        a(f'        source      : {sc}.SievePipeline.{region}.{pfx}_MarkA;')
        a(f'        destination : {sc}.SievePipeline.{region}.{pfx}_Done;')
        a(f'        trigger     : step;')
        a(f'        guard       : ({mvar} > {n});')
        a(f'        action      : {{}}')
        a(f'      }}')
        a(f'')
        a(f'      transition t_{pfx.lower()}_mB_done {{')
        a(f'        source      : {sc}.SievePipeline.{region}.{pfx}_MarkB;')
        a(f'        destination : {sc}.SievePipeline.{region}.{pfx}_Done;')
        a(f'        trigger     : step;')
        a(f'        guard       : ({mvar} > {n});')
        a(f'        action      : {{}}')
        a(f'      }}')
        a(f'    }}')
        a(f'')
    
    # Collector region
    a('    // Region: Collector -- waits for all sieves, then counts primes from 2..N')
    a('    state Collector {')
    a('      entry : {}')
    a('      exit  : {}')
    a('')
    a('      state CollWaiting { entry : {} exit : {} }')
    a('      state CollCountA  { entry : {} exit : {} }')
    a('      state CollCountB  { entry : {} exit : {} }')
    a('      state CollDone    { entry : {} exit : {} }')
    a('')
    a('      // Wait for all sieves to complete')
    
    # Build guard condition: all mult variables > N
    mult_checks = ' && '.join([f'(mult{p} > {n})' for p in sieve_primes])
    a(f'      transition t_coll_start {{')
    a(f'        source      : {sc}.SievePipeline.Collector.CollWaiting;')
    a(f'        destination : {sc}.SievePipeline.Collector.CollCountA;')
    a(f'        trigger     : step;')
    a(f'        guard       : {mult_checks};')
    a(f'        action      : {{ candidate := 2; primeCount := 0; }}')
    a(f'      }}')
    a('')
    
    # Counting transitions: one per candidate from 2 to N
    for num in range(2, n + 1):
        i = num - 2
        src = 'A' if i % 2 == 0 else 'B'
        dst = 'B' if i % 2 == 0 else 'A'
        nxt = num + 1
        a(f'      transition t_coll_c{src}{dst}_{num} {{')
        a(f'        source      : {sc}.SievePipeline.Collector.CollCount{src};')
        a(f'        destination : {sc}.SievePipeline.Collector.CollCount{dst};')
        a(f'        trigger     : step;')
        a(f'        guard       : (candidate = {num});')
        a(f'        action      : {{ primeCount := primeCount + p{num}; candidate := {nxt}; }}')
        a(f'      }}')
        a('')
    
    # Done transitions
    a(f'      transition t_coll_cA_done {{')
    a(f'        source      : {sc}.SievePipeline.Collector.CollCountA;')
    a(f'        destination : {sc}.SievePipeline.Collector.CollDone;')
    a(f'        trigger     : step;')
    a(f'        guard       : (candidate > {n});')
    a(f'        action      : {{}}')
    a(f'      }}')
    a('')
    a(f'      transition t_coll_cB_done {{')
    a(f'        source      : {sc}.SievePipeline.Collector.CollCountB;')
    a(f'        destination : {sc}.SievePipeline.Collector.CollDone;')
    a(f'        trigger     : step;')
    a(f'        guard       : (candidate > {n});')
    a(f'        action      : {{}}')
    a(f'      }}')
    a('    }')
    a('  }')
    a('')
    a('  // Transition from Init to concurrent pipeline')
    a(f'  transition t_start {{')
    a(f'    source      : {sc}.Init;')
    a(f'    destination : {sc}.SievePipeline;')
    a(f'    trigger     : step;')
    a(f'    guard       : true;')
    a(f'    action      : {{}}')
    a(f'  }}')
    a('}')
    
    # Write output file
    os.makedirs(os.path.dirname(output_file), exist_ok=True)
    with open(output_file, 'w') as f:
        f.write('\n'.join(lines) + '\n')
    
    # Report statistics
    total_transitions = 0
    for p in sieve_primes:
        mults = list(range(2 * p, n + 1, p))
        total_transitions += len(mults) * 2 + 2  # mark + skip per mult, + 2 done
    coll_transitions = (n - 1) + 1 + 2  # counting + start + done
    total_transitions += coll_transitions + 1  # + t_start
    
    print(f"✓ Generated {output_file}")
    print(f"  Upper bound: {n}")
    print(f"  Sieve filters: {len(sieve_primes)} (primes: {', '.join(str(p) for p in sieve_primes)})")
    print(f"  Expected primes found: {all_primes_count}")
    for p in sieve_primes:
        mults = list(range(2 * p, n + 1, p))
        print(f"    Sieve{p}: {len(mults)} multiples")
    print(f"  Total transitions: {total_transitions}")
    print(f"  Total lines: {len(lines)}")
    
    # Build test case string (avoid backslash in f-string)
    done_states = [f'S{p}_Done' for p in sieve_primes] + ['Coll_Done']
    done_states_str = ', '.join(f'"{s}"' for s in done_states)
    test_case = f'makeTestCase("data/sieve_concurrent_{n}.stb", new String[] {{"Init"}}, new String[]{{{done_states_str}}}, events);'
    print(f"  Test case: {test_case}")


def main():
    n = 49  # default
    output_file = None
    
    # Parse command-line arguments
    if len(sys.argv) > 1:
        try:
            n = int(sys.argv[1])
        except ValueError:
            print(f"Error: First argument must be an integer (got '{sys.argv[1]}')")
            sys.exit(1)
    
    if len(sys.argv) > 2:
        output_file = sys.argv[2]
    else:
        output_file = f'data/sieve_concurrent_{n}.stb'
    
    if n < 2:
        print("Error: N must be >= 2")
        sys.exit(1)
    
    generate_sieve_statechart(n, output_file)


if __name__ == '__main__':
    main()
