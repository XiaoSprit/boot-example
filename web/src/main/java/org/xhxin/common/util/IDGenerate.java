package org.xhxin.common.util;

import java.util.Random;

public class IDGenerate {

    private static final long startTimeStamp = 1534919378079L; //定义一个起始时间 new Date().getTime()

    private static final long workerIdBits = 6L;
    private static final long dataCenterIdBits = 6L;
    private static final long maxWorkerId = -1L ^ (-1L << workerIdBits);
    private static final long maxDataCenterId = -1L ^ (-1L << dataCenterIdBits);

    private static final long sequenceBits = 14L;
    private static final long workerIdShift = sequenceBits;
    private static final long dataCenterIdShift = sequenceBits + workerIdBits;
    private static final long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;
    private static final long sequenceMask = -1L ^ (-1L << sequenceBits);
    private static final Random r = new Random();

    private final long workerId;
    private final long dataCenterId;
    private final long idEpoch;
    private long lastTimestamp = -1L;
    private long sequence = 0;

    public IDGenerate() {
        this(startTimeStamp);
    }

    public IDGenerate(long idEpoch) {
        this(r.nextInt((int) maxWorkerId), r.nextInt((int) maxDataCenterId), 0, idEpoch);
    }

    public IDGenerate(long workerId, long dataCenterId, long sequence) {
        this(workerId, dataCenterId, sequence, startTimeStamp);
    }

    public IDGenerate(long workerId, long dataCenterId, long sequence, long idEpoch) {
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
        this.sequence = sequence;
        this.idEpoch = idEpoch;

        if (workerId < 0 || workerId > maxWorkerId) {
            throw new IllegalArgumentException("workerId is illegal: " + workerId);
        }
        if (dataCenterId < 0 || dataCenterId > maxDataCenterId) {
            throw new IllegalArgumentException("dataCenterId is illegal: " + dataCenterId);
        }

        if (idEpoch >= timeGen()) {
            throw new IllegalArgumentException("idEpoch is illegal: " + idEpoch);
        }
    }

    public long getDataCenterId() {
        return dataCenterId;
    }

    public long getWorkerId() {
        return workerId;
    }

    public long getTime() {
        return timeGen();
    }

    public synchronized long nextId() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            throw new IllegalArgumentException("Clock moved backwards.");
        }

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = timestamp;
        long id = ((timestamp - idEpoch) << timestampLeftShift) | (dataCenterId <<
                dataCenterIdShift) | (workerId << workerIdShift) | sequence;
        return id;
    }

    public long getIdTimestamp(long id) {
        return idEpoch + (id >> timestampLeftShift);
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }

        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

}