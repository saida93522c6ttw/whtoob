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
package com.alipay.sofa.rpc.config;

import com.alipay.sofa.rpc.common.RpcConstants;
import com.alipay.sofa.rpc.common.utils.ExceptionUtils;
import com.alipay.sofa.rpc.common.utils.NetUtils;
import com.alipay.sofa.rpc.common.utils.StringUtils;
import com.alipay.sofa.rpc.listener.ChannelListener;
import com.alipay.sofa.rpc.server.Server;
import com.alipay.sofa.rpc.server.ServerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.alipay.sofa.rpc.common.RpcConfigs.getBooleanValue;
import static com.alipay.sofa.rpc.common.RpcConfigs.getIntValue;
import static com.alipay.sofa.rpc.common.RpcConfigs.getStringValue;
import static com.alipay.sofa.rpc.common.RpcOptions.DEFAULT_PROTOCOL;
import static com.alipay.sofa.rpc.common.RpcOptions.DEFAULT_SERIALIZATION;
import static com.alipay.sofa.rpc.common.RpcOptions.DEFAULT_TRANSPORT;
import static com.alipay.sofa.rpc.common.RpcOptions.SERVER_ACCEPTS;
import static com.alipay.sofa.rpc.common.RpcOptions.SERVER_CONTEXT_PATH;
import static com.alipay.sofa.rpc.common.RpcOptions.SERVER_DAEMON;
import static com.alipay.sofa.rpc.common.RpcOptions.SERVER_EPOLL;
import static com.alipay.sofa.rpc.common.RpcOptions.SERVER_HOST;
import static com.alipay.sofa.rpc.common.RpcOptions.SERVER_IOTHREADS;
import static com.alipay.sofa.rpc.common.RpcOptions.SERVER_POOL_ALIVETIME;
import static com.alipay.sofa.rpc.common.RpcOptions.SERVER_POOL_CORE;
import static com.alipay.sofa.rpc.common.RpcOptions.SERVER_POOL_MAX;
import static com.alipay.sofa.rpc.common.RpcOptions.SERVER_POOL_PRE_START;
import static com.alipay.sofa.rpc.common.RpcOptions.SERVER_POOL_QUEUE;
import static com.alipay.sofa.rpc.common.RpcOptions.SERVER_POOL_QUEUE_TYPE;
import static com.alipay.sofa.rpc.common.RpcOptions.SERVER_POOL_TYPE;
import static com.alipay.sofa.rpc.common.RpcOptions.SERVER_PORT_START;
import static com.alipay.sofa.rpc.common.RpcOptions.SERVER_STOP_TIMEOUT;
import static com.alipay.sofa.rpc.common.RpcOptions.SERVER_TELNET;
import static com.alipay.sofa.rpc.common.RpcOptions.SEVER_ADAPTIVE_PORT;
import static com.alipay.sofa.rpc.common.RpcOptions.SEVER_AUTO_START;
import static com.alipay.sofa.rpc.common.RpcOptions.TRANSPORT_PAYLOAD_MAX;
import static com.alipay.sofa.rpc.common.RpcOptions.TRANSPORT_SERVER_KEEPALIVE;

/**
 * ???????????????
 *
 * @author <a href=mailto:zhanggeng.zg@antfin.com>GengZhang</a>
 */
public class ServerConfig extends AbstractIdConfig implements Serializable {
    /**
     * The constant serialVersionUID.
     */
    private static final long                 serialVersionUID = -574374673831680403L;

    /*------------- ?????????????????????-----------------*/
    /**
     * ????????????
     */
    protected String                          protocol         = getStringValue(DEFAULT_PROTOCOL);

    /**
     * ????????????IP??????????????????
     */
    protected String                          host             = getStringValue(SERVER_HOST);

    /**
     * ????????????
     */
    protected int                             port             = getIntValue(SERVER_PORT_START);

    /**
     * ????????????
     */
    protected String                          contextPath      = getStringValue(SERVER_CONTEXT_PATH);

    /**
     * io???????????????
     */
    protected int                             ioThreads        = getIntValue(SERVER_IOTHREADS);

    /**
     * ???????????????
     */
    protected String                          threadPoolType   = getStringValue(SERVER_POOL_TYPE);

    /**
     * ?????????????????????
     */
    protected int                             coreThreads      = getIntValue(SERVER_POOL_CORE);

    /**
     * ?????????????????????
     */
    protected int                             maxThreads       = getIntValue(SERVER_POOL_MAX);

    /**
     * ????????????telnet????????????????????????
     */
    protected boolean                         telnet           = getBooleanValue(SERVER_TELNET);

    /**
     * ???????????????????????????????????????
     */
    protected String                          queueType        = getStringValue(SERVER_POOL_QUEUE_TYPE);

    /**
     * ???????????????????????????
     */
    protected int                             queues           = getIntValue(SERVER_POOL_QUEUE);

    /**
     * ?????????????????????
     */
    protected int                             aliveTime        = getIntValue(SERVER_POOL_ALIVETIME);

    /**
     * ????????????????????????????????????
     */
    protected boolean                         preStartCore     = getBooleanValue(SERVER_POOL_PRE_START);

    /**
     * ??????????????????????????????????????????
     */
    protected int                             accepts          = getIntValue(SERVER_ACCEPTS);

    /**
     * ?????????????????????
     */
    @Deprecated
    protected int                             payload          = getIntValue(TRANSPORT_PAYLOAD_MAX);

    /**
     * ???????????????
     */
    protected String                          serialization    = getStringValue(DEFAULT_SERIALIZATION);

    /**
     * ?????????????????????
     */
    @Deprecated
    protected String                          dispatcher       = RpcConstants.DISPATCHER_MESSAGE;

    /**
     * The Parameters. ???????????????
     */
    protected Map<String, String>             parameters;

    /**
     * ??????ip????????????????????????1.2.3.4??????????????????????????????3.4.5.6
     */
    protected String                          virtualHost;

    /**
     * ????????????
     */
    protected Integer                         virtualPort;

    /**
     * ?????????????????????????????????????????????????????????
     */
    protected transient List<ChannelListener> onConnect;

    /**
     * ????????????epoll
     */
    protected boolean                         epoll            = getBooleanValue(SERVER_EPOLL);

    /**
     * ??????hold????????????true????????????????????????????????????false????????????????????????
     */
    protected boolean                         daemon           = getBooleanValue(SERVER_DAEMON);

    /**
     * The Adaptive port.
     */
    protected boolean                         adaptivePort     = getBooleanValue(SEVER_ADAPTIVE_PORT);

    /**
     * ?????????
     */
    protected String                          transport        = getStringValue(DEFAULT_TRANSPORT);

    /**
     * ??????????????????
     */
    protected boolean                         autoStart        = getBooleanValue(SEVER_AUTO_START);

    /**
     * ???????????????????????????
     */
    protected int                             stopTimeout      = getIntValue(SERVER_STOP_TIMEOUT);

    /**
     * ?????????????????????
     */
    protected boolean                         keepAlive        = getBooleanValue(TRANSPORT_SERVER_KEEPALIVE);

    /*------------- ?????????????????????-----------------*/
    /**
     * ???????????????
     */
    private transient volatile Server         server;

    /**
     * ??????????????????????????????????????????????????????
     */
    private transient String                  boundHost;

    /**
     * ????????????
     *
     * @return the server
     */
    public synchronized Server buildIfAbsent() {
        if (server != null) {
            return server;
        }
        // ??????????????????+???????????????
        // ConfigValueHelper.check(ProtocolType.valueOf(getProtocol()),
        //                SerializationType.valueOf(getSerialization()));

        server = ServerFactory.getServer(this);
        return server;
    }

    /**
     * ????????????
     */
    public synchronized void destroy() {
        ServerFactory.destroyServer(this);
    }

    /**
     * Gets protocol.
     *
     * @return the protocol
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Sets protocol.
     *
     * @param protocol the protocol
     * @return the protocol
     */
    public ServerConfig setProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    /**
     * Gets host.
     *
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets host.
     *
     * @param host the host
     * @return the host
     */
    public ServerConfig setHost(String host) {
        this.host = host;
        return this;
    }

    /**
     * Gets port.
     *
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * Sets port.
     *
     * @param port the port
     * @return the port
     */
    public ServerConfig setPort(int port) {
        if (!NetUtils.isRandomPort(port) && NetUtils.isInvalidPort(port)) {
            throw ExceptionUtils.buildRuntime("server.port", port + "",
                "port must between -1 and 65535 (-1 means random port)");
        }
        this.port = port;
        return this;
    }

    /**
     * Gets context path.
     *
     * @return the context path
     */
    public String getContextPath() {
        return contextPath;
    }

    /**
     * Sets context path.
     *
     * @param contextPath the context path
     * @return the context path
     */
    public ServerConfig setContextPath(String contextPath) {
        if (!contextPath.endsWith(StringUtils.CONTEXT_SEP)) {
            contextPath += StringUtils.CONTEXT_SEP;
        }
        this.contextPath = contextPath;
        return this;
    }

    /**
     * Gets ioThreads.
     *
     * @return the ioThreads
     */
    public int getIoThreads() {
        return ioThreads;
    }

    /**
     * Sets ioThreads.
     *
     * @param ioThreads the ioThreads
     * @return the ioThreads
     */
    public ServerConfig setIoThreads(int ioThreads) {
        this.ioThreads = ioThreads;
        return this;
    }

    /**
     * Gets threadPoolType.
     *
     * @return the threadPoolType
     */
    public String getThreadPoolType() {
        return threadPoolType;
    }

    /**
     * Sets threadPoolType.
     *
     * @param threadPoolType the threadPoolType
     * @return the threadPoolType
     */
    public ServerConfig setThreadPoolType(String threadPoolType) {
        this.threadPoolType = threadPoolType;
        return this;
    }

    /**
     * Gets core threads.
     *
     * @return the core threads
     */
    public int getCoreThreads() {
        return coreThreads;
    }

    /**
     * Sets core threads.
     *
     * @param coreThreads the core threads
     * @return the core threads
     */
    public ServerConfig setCoreThreads(int coreThreads) {
        this.coreThreads = coreThreads;
        return this;
    }

    /**
     * Gets max threads.
     *
     * @return the max threads
     */
    public int getMaxThreads() {
        return maxThreads;
    }

    /**
     * Sets max threads.
     *
     * @param maxThreads the max threads
     * @return the max threads
     */
    public ServerConfig setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
        return this;
    }

    /**
     * Is telnet boolean.
     *
     * @return the boolean
     */
    public boolean isTelnet() {
        return telnet;
    }

    /**
     * Sets telnet.
     *
     * @param telnet the telnet
     * @return the telnet
     */
    public ServerConfig setTelnet(boolean telnet) {
        this.telnet = telnet;
        return this;
    }

    /**
     * Gets queue type.
     *
     * @return the queue type
     */
    public String getQueueType() {
        return queueType;
    }

    /**
     * Sets queue type.
     *
     * @param queueType the queue type
     * @return the queue type
     */
    public ServerConfig setQueueType(String queueType) {
        this.queueType = queueType;
        return this;
    }

    /**
     * Gets queues.
     *
     * @return the queues
     */
    public int getQueues() {
        return queues;
    }

    /**
     * Sets queues.
     *
     * @param queues the queues
     * @return the queues
     */
    public ServerConfig setQueues(int queues) {
        this.queues = queues;
        return this;
    }

    /**
     * Gets alive time.
     *
     * @return the alive time
     */
    public int getAliveTime() {
        return aliveTime;
    }

    /**
     * Sets alive time.
     *
     * @param aliveTime the alive time
     * @return the alive time
     */
    public ServerConfig setAliveTime(int aliveTime) {
        this.aliveTime = aliveTime;
        return this;
    }

    /**
     * Is pre start core boolean.
     *
     * @return the boolean
     */
    public boolean isPreStartCore() {
        return preStartCore;
    }

    /**
     * Sets pre start core.
     *
     * @param preStartCore the pre start core
     * @return the pre start core
     */
    public ServerConfig setPreStartCore(boolean preStartCore) {
        this.preStartCore = preStartCore;
        return this;
    }

    /**
     * Gets accepts.
     *
     * @return the accepts
     */
    public int getAccepts() {
        return accepts;
    }

    /**
     * Sets accepts.
     *
     * @param accepts the accepts
     * @return the accepts
     */
    public ServerConfig setAccepts(int accepts) {
        ConfigValueHelper.checkPositiveInteger("server.accept", accepts);
        this.accepts = accepts;
        return this;
    }

    /**
     * Gets payload.
     *
     * @return the payload
     */
    public int getPayload() {
        return payload;
    }

    /**
     * Sets payload.
     *
     * @param payload the payload
     * @return the payload
     */
    public ServerConfig setPayload(int payload) {
        this.payload = payload;
        return this;
    }

    /**
     * Gets serialization.
     *
     * @return the serialization
     */
    public String getSerialization() {
        return serialization;
    }

    /**
     * Sets serialization.
     *
     * @param serialization the serialization
     * @return the serialization
     */
    public ServerConfig setSerialization(String serialization) {
        this.serialization = serialization;
        return this;
    }

    /**
     * Gets dispatcher.
     *
     * @return the dispatcher
     */
    public String getDispatcher() {
        return dispatcher;
    }

    /**
     * Sets dispatcher.
     *
     * @param dispatcher the dispatcher
     * @return the dispatcher
     */
    public ServerConfig setDispatcher(String dispatcher) {
        this.dispatcher = dispatcher;
        return this;
    }

    /**
     * Gets parameters.
     *
     * @return the parameters
     */
    public Map<String, String> getParameters() {
        return parameters;
    }

    /**
     * Sets parameters.
     *
     * @param parameters the parameters
     * @return the parameters
     */
    public ServerConfig setParameters(Map<String, String> parameters) {
        if (this.parameters == null) {
            this.parameters = new ConcurrentHashMap<String, String>();
            this.parameters.putAll(parameters);
        }
        return this;
    }

    /**
     * Gets virtualHost.
     *
     * @return the virtualHost
     */
    public String getVirtualHost() {
        return virtualHost;
    }

    /**
     * Sets virtualHost.
     *
     * @param virtualHost the virtualHost
     * @return the virtualHost
     */
    public ServerConfig setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
        return this;
    }

    /**
     * Gets virtual port.
     *
     * @return the virtual port
     */
    public Integer getVirtualPort() {
        return virtualPort;
    }

    /**
     * Sets virtual port.
     *
     * @param virtualPort the virtual port
     * @return the virtual port
     */
    public ServerConfig setVirtualPort(Integer virtualPort) {
        this.virtualPort = virtualPort;
        return this;
    }

    /**
     * Gets onConnect.
     *
     * @return the onConnect
     */
    public List<ChannelListener> getOnConnect() {
        return onConnect;
    }

    /**
     * Sets onConnect.
     *
     * @param onConnect the onConnect
     * @return the onConnect
     */
    public ServerConfig setOnConnect(List<ChannelListener> onConnect) {
        this.onConnect = onConnect;
        return this;
    }

    /**
     * Is epoll boolean.
     *
     * @return the boolean
     */
    public boolean isEpoll() {
        return epoll;
    }

    /**
     * Sets epoll.
     *
     * @param epoll the epoll
     * @return the epoll
     */
    public ServerConfig setEpoll(boolean epoll) {
        this.epoll = epoll;
        return this;
    }

    /**
     * Is daemon boolean.
     *
     * @return the boolean
     */
    public boolean isDaemon() {
        return daemon;
    }

    /**
     * Sets daemon.
     *
     * @param daemon the daemon
     * @return the daemon
     */
    public ServerConfig setDaemon(boolean daemon) {
        this.daemon = daemon;
        return this;
    }

    /**
     * Gets transport.
     *
     * @return the transport
     */
    public String getTransport() {
        return transport;
    }

    /**
     * Sets transport.
     *
     * @param transport the transport
     * @return the transport
     */
    public ServerConfig setTransport(String transport) {
        this.transport = transport;
        return this;
    }

    /**
     * Is adaptive port boolean.
     *
     * @return the boolean
     */
    public boolean isAdaptivePort() {
        return adaptivePort;
    }

    /**
     * Sets adaptive port.
     *
     * @param adaptivePort the adaptive port
     * @return the adaptive port
     */
    public ServerConfig setAdaptivePort(boolean adaptivePort) {
        this.adaptivePort = adaptivePort;
        return this;
    }

    /**
     * Is auto start boolean.
     *
     * @return the boolean
     */
    public boolean isAutoStart() {
        return autoStart;
    }

    /**
     * Sets auto start.
     *
     * @param autoStart the auto start
     * @return the auto start
     */
    public ServerConfig setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
        return this;
    }

    /**
     * Gets stop timeout.
     *
     * @return the stop timeout
     */
    public int getStopTimeout() {
        return stopTimeout;
    }

    /**
     * Sets stop timeout.
     *
     * @param stopTimeout the stop timeout
     * @return the stop timeout
     */
    public ServerConfig setStopTimeout(int stopTimeout) {
        this.stopTimeout = stopTimeout;
        return this;
    }

    /**
     * Gets server.
     *
     * @return the server
     */
    public Server getServer() {
        return server;
    }

    /**
     * Set server
     * @param server
     */
    public void setServer(Server server) {
        this.server = server;
    }

    /**
     * Sets bound host.
     *
     * @param boundHost the bound host
     * @return the bound host
     */
    public ServerConfig setBoundHost(String boundHost) {
        this.boundHost = boundHost;
        return this;
    }

    /**
     * Gets bound host
     *
     * @return bound host
     */
    public String getBoundHost() {
        return boundHost;
    }

    /**
     * Get KeepAlive
     *
     * @return ???????????????
     */
    public boolean isKeepAlive() {
        return keepAlive;
    }

    /**
     * set KeepAlive
     *
     * @param keepAlive ???????????????
     * @return this
     */
    public ServerConfig setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((host == null) ? 0 : host.hashCode());
        result = prime * result + port;
        result = prime * result
            + ((protocol == null) ? 0 : protocol.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ServerConfig other = (ServerConfig) obj;
        if (host == null) {
            if (other.host != null) {
                return false;
            }
        } else if (!host.equals(other.host)) {
            return false;
        }
        if (port != other.port) {
            return false;
        }
        if (protocol == null) {
            if (other.protocol != null) {
                return false;
            }
        } else if (!protocol.equals(other.protocol)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ServerConfig [protocol=" + protocol + ", port=" + port + ", host=" + host + "]";
    }

}
