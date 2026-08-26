/**
 * @type {TextToSpeech}
 * @constructor {TextToSpeech}
 * @return {TextToSpeech}
 */
function TextToSpeech() {
    this.synth = window.speechSynthesis;
    this.voices = []; // 缓存语音列表

    if (this.synth) {
        // 监听语音列表加载完成事件，解决部分浏览器异步加载的问题
        this.synth.onvoiceschanged = () => {
            this.voices = this.synth.getVoices();
        };
    }

}

TextToSpeech.isSupported = function () {
    return 'speechSynthesis' in window && 'SpeechSynthesisUtterance' in window;
}

TextToSpeech.prototype.isSupported = function () {
    return TextToSpeech.isSupported();
}

TextToSpeech.prototype.getSynth = function () {
    return this.synth;
}

TextToSpeech.prototype.getVoices = function () {
    return this.voices;
}

TextToSpeech.prototype.isPlaying = function () {
    if (!this.isSupported()) {
        return false;
    }
    return this.synth.speaking && !this.synth.paused;
}

TextToSpeech.prototype.isPause = function () {
    if (!this.isSupported()) {
        return false;
    }
    return this.synth.paused;
}


TextToSpeech.prototype.pause = function () {
    if (!this.isSupported()) {
        return
    }
    this.synth.pause();
}

TextToSpeech.prototype.resume = function () {
    if (!this.isSupported()) {
        return
    }
    this.synth.resume();
}

TextToSpeech.prototype.togglePause = function () {
    if (!this.isSupported()) {
        return
    }

    if (this.isPlaying()) {
        // 正在发声且未暂停 -> 执行暂停
        this.pause();
    } else if (this.isPause()) {
        // 已暂停 -> 恢复播放
        this.resume();
    }
}

TextToSpeech.prototype.speak = function (text, options = {}) {
    if (!this.isSupported()) {
        console.warn('当前浏览器不支持 Web Speech API');
        return;
    }

    // 在开始新的朗读前，先取消正在进行的语音，防止声音重叠
    this.stop();

    const utterance = new SpeechSynthesisUtterance(text);

    // 设置默认参数，并允许通过 options 覆盖
    utterance.lang = options.lang || 'zh-CN'; // 默认中文
    utterance.rate = options.rate || 1.0;     // 语速 (0.1 - 10)
    utterance.pitch = options.pitch || 1.0;   // 音调 (0 - 2)
    utterance.volume = options.volume || 1.0; // 音量 (0 - 1)

    // 核心：如果传入了 voiceName，则尝试匹配语音
    if (options.voiceName && this.voices.length > 0) {
        const targetVoice = this.voices.find(voice =>
            voice.name.includes(options.voiceName)
        );
        if (targetVoice) {
            utterance.voice = targetVoice;
        } else {
            console.warn(`未找到包含 "${options.voiceName}" 的语音包，将使用默认语音`);
        }
    }

    // 执行朗读
    this.synth.speak(utterance);
}

TextToSpeech.prototype.stop = function () {
    if (!this.isSupported()) {
        return
    }
    this.synth.cancel();
}