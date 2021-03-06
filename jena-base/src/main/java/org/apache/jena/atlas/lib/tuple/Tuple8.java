/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jena.atlas.lib.tuple;

import java.util.Objects;
import java.util.function.Function;

/**
 * A tuple of 8 items.
 */
public class Tuple8<X> extends TupleBase<X> {
    protected final X x1 ;
    protected final X x2 ;
    protected final X x3 ;
    protected final X x4 ;
    protected final X x5 ;
    protected final X x6 ;
    protected final X x7 ;
    protected final X x8 ;

    protected Tuple8(X x1, X x2, X x3, X x4, X x5, X x6, X x7, X x8) {
        this.x1 = x1 ;
        this.x2 = x2 ;
        this.x3 = x3 ;
        this.x4 = x4 ;
        this.x5 = x5 ;
        this.x6 = x6 ;
        this.x7 = x7 ;
        this.x8 = x8 ;
    }

    @Override
    public final X get(int i) {
        switch (i) {
            case 0: return x1 ;
            case 1: return x2 ;
            case 2: return x3 ;
            case 3: return x4 ;
            case 4: return x5 ;
            case 5: return x6 ;
            case 6: return x7 ;
            case 7: return x8 ;
        }
        throw new IndexOutOfBoundsException() ;
    }

    @Override
    public final int len() {
        return 8 ;
    }

    @Override
    public <Y> Tuple<Y> map(Function<X, Y> function) {
        return new Tuple8<>(
                function.apply(x1),
                function.apply(x2),
                function.apply(x3),
                function.apply(x4),
                function.apply(x5),
                function.apply(x6),
                function.apply(x7),
                function.apply(x8)
                );
    }

    @Override
    public boolean contains(X item) {
        if ( Objects.equals(x1, item) ) return true;
        if ( Objects.equals(x2, item) ) return true;
        if ( Objects.equals(x3, item) ) return true;
        if ( Objects.equals(x4, item) ) return true;
        if ( Objects.equals(x5, item) ) return true;
        if ( Objects.equals(x6, item) ) return true;
        if ( Objects.equals(x7, item) ) return true;
        if ( Objects.equals(x8, item) ) return true;
        return false;
    }
}
