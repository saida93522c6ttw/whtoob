import { defineStore } from 'pinia';

interface JWTState {
  value: string | undefined
}

export const useJWTStore = defineStore({
  id: 'JWT',
  state: (): JWTState => {
    return {
      value: undefined
    }
  },
  actions: {
    /**
     * 设值
     */
    set(value: string) {
      this.value = value
    },
    /**
     * 清空
     */
    reset() {
      this.value = undefined
    }
  },
  persist: {
    enabled: true
  }
})
