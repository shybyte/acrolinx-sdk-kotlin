package com.acrolinx.client.sdk

interface InteractiveCallback<T> {
    fun run(event: T)
}