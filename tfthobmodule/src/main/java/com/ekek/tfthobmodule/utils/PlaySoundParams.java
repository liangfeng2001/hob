package com.ekek.tfthobmodule.utils;

import android.media.AudioManager;

public class PlaySoundParams {

    // Fields
    private int taskType;
    private int resource;
    private int soundType;
    private boolean looping;
    private int playingDuration;
    private int postponeDuration;
    private int streamType;
    private float volume;

    // Constructors
    public PlaySoundParams(
            int taskType,
            int resource,
            int soundType,
            boolean looping,
            int playingDuration,
            int postponeDuration,
            int streamType,
            float volume) {
        this.taskType = taskType;
        this.resource = resource;
        this.soundType = soundType;
        this.looping = looping;
        this.playingDuration = playingDuration;
        this.postponeDuration = postponeDuration;
        this.streamType = streamType;
        this.volume = volume;
    }
    public PlaySoundParams(
            int taskType,
            int resource,
            int soundType,
            boolean looping,
            int playingDuration,
            int postponeDuration,
            int streamType) {
        this(taskType, resource, soundType, looping, playingDuration, postponeDuration, streamType, 1.0f);
    }
    public PlaySoundParams(
            int taskType,
            int resource,
            int soundType,
            boolean looping,
            int playingDuration,
            int postponeDuration) {
        this(taskType, resource, soundType, looping, playingDuration, postponeDuration, AudioManager.STREAM_MUSIC);
    }
    public PlaySoundParams(
            int taskType,
            int resource,
            int soundType,
            boolean looping) {
        this(taskType, resource, soundType, looping, 0, 0);
    }
    public PlaySoundParams(
            int taskType,
            int resource,
            int soundType) {
        this(taskType, resource, soundType, false);
    }

    // Properties
    public int getTaskType() {
        return taskType;
    }
    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }
    public int getResource() {
        return resource;
    }
    public void setResource(int resource) {
        this.resource = resource;
    }
    public int getSoundType() {
        return soundType;
    }
    public void setSoundType(int soundType) {
        this.soundType = soundType;
    }
    public int getStreamType() {
        return streamType;
    }
    public void setStreamType(int streamType) {
        this.streamType = streamType;
    }
    public float getVolume() {
        return volume;
    }
    public void setVolume(float volume) {
        this.volume = volume;
    }
    public boolean isLooping() {
        return looping;
    }
    public void setLooping(boolean looping) {
        this.looping = looping;
    }
    public int getPlayingDuration() {
        return playingDuration;
    }
    public void setPlayingDuration(int playingDuration) {
        this.playingDuration = playingDuration;
    }
    public int getPostponeDuration() {
        return postponeDuration;
    }
    public void setPostponeDuration(int postponeDuration) {
        this.postponeDuration = postponeDuration;
    }
}
