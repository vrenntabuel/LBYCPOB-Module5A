/**
 * BackgroundMusicManager - browser equivalent of the JavaFX MediaPlayer-based manager.
 * Backed by HTMLAudioElement instead of javafx.scene.media.MediaPlayer, since
 * audio playback in a web app happens client-side in the browser, not on the server.
 */
class BackgroundMusicManager {
    constructor() {
        this.tracks = new Map();       // trackName -> HTMLAudioElement
        this.currentPlayer = null;
        this.currentTrack = null;
        this.globalVolume = 0.5;
        this.isMuted = false;
        this.volumeBeforeMute = this.globalVolume;
    }

    static getInstance() {
        if (!BackgroundMusicManager._instance) {
            BackgroundMusicManager._instance = new BackgroundMusicManager();
        }
        return BackgroundMusicManager._instance;
    }

    /**
     * @param trackName unique identifier for the track
     * @param resourcePath web path to the audio file, e.g. "/audio/pokemon_theme.mp3"
     */
    loadMusic(trackName, resourcePath) {
        try {
            const audio = new Audio(resourcePath);
            audio.loop = true;                 // MediaPlayer.INDEFINITE equivalent
            audio.volume = this.isMuted ? 0.0 : this.globalVolume;
            audio.preload = 'auto';

            audio.addEventListener('error', () => {
                console.error('Error playing music:', trackName, audio.error);
            });

            this.tracks.set(trackName, audio);
            console.log('Music loaded successfully:', trackName);
            return true;
        } catch (e) {
            console.error(`Failed to load music '${trackName}':`, e);
            return false;
        }
    }

    /**
     * Play a track. Must be called from inside a user gesture handler (click/tap)
     * the first time, due to browser autoplay restrictions.
     */
    async playMusic(trackName) {
        const player = this.tracks.get(trackName);
        if (!player) {
            console.error('Music track not found:', trackName);
            return false;
        }
        try {
            if (this.currentPlayer && this.currentPlayer !== player) {
                this.currentPlayer.pause();
                this.currentPlayer.currentTime = 0;
            }
            this.currentPlayer = player;
            this.currentTrack = trackName;

            await player.play();   // returns a Promise; rejects if no user gesture yet
            console.log('Playing music:', trackName);
            return true;
        } catch (e) {
            console.warn(`Autoplay blocked for '${trackName}' - waiting for user interaction.`, e);
            return false;
        }
    }

    pauseMusic() {
        if (this.currentPlayer) {
            this.currentPlayer.pause();
            console.log('Music paused:', this.currentTrack);
        }
    }

    resumeMusic() {
        if (this.currentPlayer) {
            this.currentPlayer.play();
            console.log('Music resumed:', this.currentTrack);
        }
    }

    stopMusic() {
        if (this.currentPlayer) {
            this.currentPlayer.pause();
            this.currentPlayer.currentTime = 0;
            console.log('Music stopped:', this.currentTrack);
            this.currentPlayer = null;
            this.currentTrack = null;
        }
    }

    stopAllMusic() {
        this.tracks.forEach(player => { player.pause(); player.currentTime = 0; });
        this.currentPlayer = null;
        this.currentTrack = null;
        console.log('All music stopped');
    }

    setVolume(volume) {
        volume = Math.max(0, Math.min(1, volume));
        this.globalVolume = volume;
        this.tracks.forEach(player => { player.volume = this.isMuted ? 0.0 : this.globalVolume; });
        console.log('Volume set to:', (volume * 100) + '%');
    }

    getVolume() { return this.globalVolume; }

    mute() {
        if (!this.isMuted) {
            this.volumeBeforeMute = this.globalVolume;
            this.isMuted = true;
            this.tracks.forEach(player => { player.volume = 0.0; });
            console.log('Music muted');
        }
    }

    unmute() {
        if (this.isMuted) {
            this.isMuted = false;
            this.tracks.forEach(player => { player.volume = this.globalVolume; });
            console.log('Music unmuted');
        }
    }

    toggleMute() { this.isMuted ? this.unmute() : this.mute(); }

    getIsMuted() { return this.isMuted; }

    isPlaying() {
        return this.currentPlayer !== null && !this.currentPlayer.paused;
    }

    getCurrentTrack() { return this.currentTrack; }

    setLoop(loop) {
        if (this.currentPlayer) this.currentPlayer.loop = loop;
    }

    seekTo(seconds) {
        if (this.currentPlayer) this.currentPlayer.currentTime = seconds;
    }

    getCurrentTime() {
        return this.currentPlayer ? this.currentPlayer.currentTime : 0.0;
    }

    getTotalDuration() {
        return (this.currentPlayer && !isNaN(this.currentPlayer.duration)) ? this.currentPlayer.duration : 0.0;
    }

    dispose() {
        this.stopAllMusic();
        this.tracks.forEach(player => { player.src = ''; });
        this.tracks.clear();
        console.log('BackgroundMusicManager disposed');
    }

    removeTrack(trackName) {
        const player = this.tracks.get(trackName);
        if (player) {
            if (player === this.currentPlayer) this.stopMusic();
            player.src = '';
            this.tracks.delete(trackName);
            console.log('Track removed:', trackName);
        }
    }

    getLoadedTracks() { return Array.from(this.tracks.keys()); }
}