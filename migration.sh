#!/bin/bash


# Select environment
select_env() {
    options=("local" "test" "production")
    cursor=0

    print_env_options() {
        for i in "${!options[@]}"; do
            if [ $i -eq $cursor ]; then
                echo -e " ${options[$i]}" | grep --color "${options[$i]}"
            else
                echo "  ${options[$i]}"
            fi
        done
    }

    clear_screen
    echo "Use arrow keys to navigate and press Enter to select environment: "
    print_env_options

    while true; do
        read -rsn1 key
        case $key in
            "A") # Up arrow key
                ((cursor--))
                if [ $cursor -lt 0 ]; then
                    cursor=$((${#options[@]} - 1))
                fi
                clear_screen
                echo "Use arrow keys to navigate and press Enter to select environment: "
                print_env_options
                ;;
            "B") # Down arrow key
                ((cursor++))
                if [ $cursor -ge ${#options[@]} ]; then
                    cursor=0
                fi
                clear_screen
                echo "Use arrow keys to navigate and press Enter to select environment: "
                print_env_options
                ;;
            "") # Enter key
                selected_env="${options[$cursor]}"
                clear_screen
                break
                ;;
        esac
        ((counter++))
    done
}

# Options for Flyway tasks
options=("Flyway Info" "Migrate" "Repair" "Exit")

# Initialize cursor position
cursor=0
counter=0

# Function to clear the screen
clear_screen() {
    clear
    echo "Use arrow keys to navigate and press Enter to select: "
}

# Print options with highlighting
print_options() {
    for i in "${!options[@]}"; do
        if [ $i -eq $cursor ]; then
            echo -e " ${options[$i]}" | grep --color "${options[$i]}" # Highlighted in red
        else
            echo "  ${options[$i]}"
        fi
    done
}

# Clear the screen and print initial options
clear_screen
print_options

# Read key inputs
while [ "$counter" -lt 1 ]; do
    read -rsn1 key
    case $key in
        "A") # Up arrow key
            ((cursor--))
            if [ $cursor -lt 0 ]; then
                cursor=$((${#options[@]} - 1))
            fi
            clear_screen
            print_options
            ;;
        "B") # Down arrow key
            ((cursor++))
            if [ $cursor -ge ${#options[@]} ]; then
                cursor=0
            fi
            clear_screen
            print_options
            ;;
        "") # Enter key
            clear_screen
            selected_option="${options[$cursor]}"
            case $selected_option in
                "Flyway Info")
                    select_env
                    echo "Running Flyway info for environment: ${selected_env}"
                    ./gradlew flywayInfo -Pprofile=$selected_env
                    ;;
                "Migrate")
                    select_env
                    echo "Running Flyway migrate for environment: ${selected_env}"
                    ./gradlew flywayMigrate -Pprofile=$selected_env
                    ;;
                "Repair")
                    select_env
                    echo "Running Flyway repair for environment: ${selected_env}"
                    ./gradlew flywayRepair -Pprofile=$selected_env
                    ;;
                "Exit")
                    break
                    ;;
                *) echo "Invalid option";;
            esac
            ;;
    esac
done



