import { useDispatch, useSelector } from 'react-redux';
import type { RootState, AppDispatch } from './index';

// Použijte tyto hooks místo plain `useDispatch` a `useSelector`
export const useAppDispatch = useDispatch.withTypes<AppDispatch>();
export const useAppSelector = useSelector.withTypes<RootState>();
