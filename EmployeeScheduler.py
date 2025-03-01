import tkinter as tk
from tkinter import messagebox
import random

class EmployeeSchedulerApp:
    def __init__(self, root):
        self.root = root
        self.root.title("Shreya's Scheduler")
        self.root.configure(bg="#cceeff")  # Light blue background

        # Days and shifts
        self.DAYS = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
        self.SHIFTS = ["Morning", "Afternoon", "Evening"]

        # Initialize the schedule and employee preferences
        self.schedule = {day: {"Morning": [], "Afternoon": [], "Evening": []} for day in self.DAYS}
        self.employee_preferences = {}
        self.days_worked = {}

        # Title label
        title_label = tk.Label(self.root, text="Shreya's Employee Shift Scheduler", font=("Arial", 16), bg="#cceeff", fg="#333333")
        title_label.pack(pady=10)

        # Employee name and shift input
        self.name_label = tk.Label(self.root, text="Employee Name:", font=("Arial", 12), bg="#cceeff")
        self.name_label.pack(pady=2)
        self.name_entry = tk.Entry(self.root, font=("Arial", 12))
        self.name_entry.pack(pady=2)

        self.shift_label = tk.Label(self.root, text="Shift Preference:", font=("Arial", 12), bg="#cceeff")
        self.shift_label.pack(pady=2)
        self.shift_var = tk.StringVar(self.root)
        self.shift_var.set("Morning")
        self.shift_menu = tk.OptionMenu(self.root, self.shift_var, "Morning", "Afternoon", "Evening")
        self.shift_menu.config(font=("Arial", 12))
        self.shift_menu.pack(pady=2)

        self.submit_button = tk.Button(self.root, text="Submit", command=self.submit, bg="#6495ED", fg="white", font=("Arial", 12))
        self.submit_button.pack(pady=10)

        self.schedule_button = tk.Button(self.root, text="Schedule Shifts", command=self.schedule_shifts, bg="#32CD32", fg="white", font=("Arial", 12))
        self.schedule_button.pack(pady=10)

        # Output area
        self.output_text = tk.Text(self.root, height=15, width=50, font=("Arial", 12))
        self.output_text.pack(pady=10)
        self.output_text.config(state=tk.DISABLED)

    def submit(self):
        name = self.name_entry.get()
        shift = self.shift_var.get()
        if name:
            if name not in self.employee_preferences:
                self.employee_preferences[name] = []
            self.employee_preferences[name].append(shift)
            self.name_entry.delete(0, tk.END)
        else:
            messagebox.showerror("Input Error", "Employee Name cannot be empty!")

    def schedule_shifts(self):
        # Initialize the days worked for each employee
        self.days_worked = {employee: 0 for employee in self.employee_preferences}

        # Clear the schedule before we start
        self.schedule = {day: {"Morning": [], "Afternoon": [], "Evening": []} for day in self.DAYS}

        # Step 1: Try to assign employees to their preferred shifts
        for employee, preferences in self.employee_preferences.items():
            for shift in preferences:
                self.assign_shift(employee, shift)

        # Step 2: Fill any shifts with fewer than 2 employees, without calling this on every request
        self.fill_remaining_shifts()

        # Step 3: Display the schedule
        self.display_schedule()

    def assign_shift(self, employee, shift):
        # Try to assign the employee to their preferred shift for each day
        for day in self.DAYS:
            if self.days_worked[employee] < 5:
                shift_list = self.schedule[day][shift]
                # Only assign if shift is not full and employee has not worked this day
                if len(shift_list) < 2 and employee not in shift_list:
                    shift_list.append(employee)
                    self.days_worked[employee] += 1
                    break  # Break after the first successful assignment

    def fill_remaining_shifts(self):
        # Ensure all shifts have at least 2 employees
        for day in self.DAYS:
            for shift in self.SHIFTS:
                shift_list = self.schedule[day][shift]
                while len(shift_list) < 2:
                    # Randomly assign an employee who has not worked 5 days yet
                    available_employees = [employee for employee, days in self.days_worked.items() if days < 5 and employee not in shift_list]
                    if available_employees:
                        selected_employee = random.choice(available_employees)
                        shift_list.append(selected_employee)
                        self.days_worked[selected_employee] += 1
                    else:
                        break

    def display_schedule(self):
        schedule_text = ""
        for day, shifts in self.schedule.items():
            schedule_text += f"{day}:\n"
            for shift, employees in shifts.items():
                schedule_text += f"  {shift}: {', '.join(employees)}\n"

        self.output_text.config(state=tk.NORMAL)
        self.output_text.delete(1.0, tk.END)
        self.output_text.insert(tk.END, schedule_text)
        self.output_text.config(state=tk.DISABLED)

if __name__ == "__main__":
    root = tk.Tk()
    app = EmployeeSchedulerApp(root)
    root.mainloop()
