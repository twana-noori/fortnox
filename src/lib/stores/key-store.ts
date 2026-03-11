import create from 'zustand';

const useKeyStore = create((set) => ({
  apiKey: '',
  setApiKey: (key) => set({ apiKey: key }),
}));

export default useKeyStore;