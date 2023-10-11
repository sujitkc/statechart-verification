// Copyright 2021 Code Intelligence GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.



import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.api.FuzzerSecurityIssueMedium;
import java.security.SecureRandom;
import java.util.*;


  public class NumberFuzzer5 {
  public static void fuzzerInitialize() {
    // Optional initialization to be run before the first call to fuzzerTestOneInput.
  }

    public static void fuzzerTestOneInput(FuzzedDataProvider data) {
      int val = data.consumeInt();
      if(val<0) val=val*-1;
      val+=1;
      System.out.println(val);
      if (val <0 ){
        mustNeverBeCalled();
      }
  }
private static void mustNeverBeCalled() {
  System.out.println("mustNeverBeCalled is Called");
  throw new FuzzerSecurityIssueMedium("mustNeverBeCalled has been called");
  }
}

