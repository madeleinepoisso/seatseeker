import { Dispatch, SetStateAction, forwardRef } from "react";

interface ControlledInputProps {
  value: string;
  setValue: Dispatch<SetStateAction<string>>;
  ariaLabel: string;
}

export const ControlledInput = forwardRef<
  HTMLInputElement,
  ControlledInputProps
>(({ value, setValue, ariaLabel }, ref) => (
  <input
    ref={ref}
    type="text"
    className="repl-command-box"
    value={value}
    placeholder="Enter command here!"
    onChange={(ev) => setValue(ev.target.value)}
    aria-label={ariaLabel}
  ></input>
));