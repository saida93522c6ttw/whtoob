/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alipay.sofa.rpc.codec.sofahessian;

import com.alipay.hessian.generic.io.GenericArraySerializer;
import com.alipay.hessian.generic.io.GenericClassDeserializer;
import com.alipay.hessian.generic.io.GenericClassSerializer;
import com.alipay.hessian.generic.io.GenericCollectionSerializer;
import com.alipay.hessian.generic.io.GenericDeserializer;
import com.alipay.hessian.generic.io.GenericMapSerializer;
import com.alipay.hessian.generic.io.GenericObjectSerializer;
import com.alipay.hessian.generic.model.GenericArray;
import com.alipay.hessian.generic.model.GenericClass;
import com.alipay.hessian.generic.model.GenericCollection;
import com.alipay.hessian.generic.model.GenericMap;
import com.alipay.hessian.generic.model.GenericObject;
import com.alipay.hessian.generic.util.ClassFilter;
import com.alipay.sofa.rpc.common.utils.StringUtils;
import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.Serializer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 
 * @author <a href="mailto:caojie.cj@antfin.com">CaoJie</a>
 */
public class GenericSingleClassLoaderSofaSerializerFactory extends SingleClassLoaderSofaSerializerFactory {

    private static final char                                ARRAY_PREFIX     = '[';

    private static final ConcurrentMap<String, Deserializer> DESERIALIZER_MAP = new ConcurrentHashMap<String, Deserializer>();

    @Override
    public Serializer getSerializer(Class cl) throws HessianProtocolException {

        if (GenericObject.class == cl) {
            return GenericObjectSerializer.getInstance();
        }

        if (GenericArray.class == cl) {
            return GenericArraySerializer.getInstance();
        }

        if (GenericCollection.class == cl) {
            return GenericCollectionSerializer.getInstance();
        }

        if (GenericMap.class == cl) {
            return GenericMapSerializer.getInstance();
        }

        if (GenericClass.class == cl) {
            return GenericClassSerializer.getInstance();
        }

        return super.getSerializer(cl);
    }

    @Override
    public Deserializer getDeserializer(String type) throws HessianProtocolException {

        // ???????????????????????????, ?????????jdk?????????, ????????????????????????
        if (StringUtils.isEmpty(type) || ClassFilter.filterExcludeClass(type)) {
            return super.getDeserializer(type);
        }

        // ?????????????????????, ??????name????????????, ??????jdk???, ????????????????????????
        if (type.charAt(0) == ARRAY_PREFIX && ClassFilter.arrayFilter(type)) {
            return super.getDeserializer(type);
        }

        // ???????????????????????????????????????
        Deserializer deserializer = DESERIALIZER_MAP.get(type);
        if (deserializer != null) {
            return deserializer;
        }

        // ?????????????????????, ?????????java.lang.Class??????GenericClassDeserializer,????????????GenericDeserializer
        if (ClassFilter.CLASS_NAME.equals(type)) {
            deserializer = GenericClassDeserializer.getInstance();
        } else {
            deserializer = new GenericDeserializer(type);
        }

        DESERIALIZER_MAP.putIfAbsent(type, deserializer);
        return deserializer;
    }
}
