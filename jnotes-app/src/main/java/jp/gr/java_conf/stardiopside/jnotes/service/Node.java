package jp.gr.java_conf.stardiopside.jnotes.service;

public record Node<E, K>(E item, K prev, K next) {
}
