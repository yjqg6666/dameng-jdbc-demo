CREATE TABLE book (
 book_id INT IDENTITY(1,1) PRIMARY KEY,
 name VARCHAR(128) NOT NULL,
 author VARCHAR(64) NOT NULL,
 publisher VARCHAR(64) NOT NULL,
 description TEXT
);

INSERT INTO book (name, author, publisher, description) VALUES('Learn to Program with Assembly: Foundational Learning for New Programmers', 'Jonathan Bartlett', 'Apress', 'Many programmers have limited effectiveness because they don’t have a deep understanding of how their computer actually works under the hood.  In Learn to Program with Assembly, you will learn to program in assembly language - the language of the computer itself. Assembly language is often thought of as a difficult and arcane subject.  However, author Jonathan Bartlett presents the material in a way that works just as well for first-time programmers as for long-time professionals.  Whether this is your first programming book ever or you are a professional wanting to deepen your understanding of the computer you are working with, this book is for you.  The book teaches 64-bit x86 assembly language running on the Linux operating system.  However, even if you are not running Linux, a provided Docker image will allow you to use a Mac or Windows computer as well. The book starts with extremely simple programs to help you get your grounding, going steadily deeper with each chapter.  At the end of the first section, you will be familiar with most of the basic instructions available on the processor that you will need for any task.  The second part deals with interactions with the operating system.  It shows how to make calls to the standard library, how to make direct system calls to the kernel, how to write your own library code, and how to work with memory.  The third part shows how modern programming language features such as exception handling, object-oriented programming, and garbage collection work at the assembly language level. Additionally, the book comes with several appendices covering various topics such as running the debugger, vector processing, optimization principles, a list of common instructions, and other important subjects. This book is the 64-bit successor to Jonathan Bartlett’s previousbook, Programming from the Ground Up, which has been a programming classic for more than 15 years.  This book covers similar ground but with modern 64-bit processors, and also includes a lot more information about how high level programming language features are implemented in assembly language. What You Will Learn How the processor operates, How computers represent data internally, How programs interact with the operating system, How to write and use dynamic code libraries, How high-level programming languages implement their features. Who This Book Is For, Anyone who wants to know how their computer really works under the hood, including first time programmers, students, and professionals. ');

INSERT INTO book (name, author, publisher, description) VALUES('x64 Assembly Language Step-by-Step: Programming with Linux', 'Jeff Duntemann', 'Wiley', '
The long-awaited x64 edition of the bestselling introduction to Intel assembly language
In the newly revised fourth edition of x64 Assembly Language Step-by-Step: Programming with Linux, author Jeff Duntemann delivers an extensively rewritten introduction to assembly language with a strong focus on 64-bit long-mode Linux assembler. The book offers a lighthearted, robust, and accessible approach to a challenging technical discipline, giving you a step-by-step path to learning assembly code that\’s engaging and easy to read.
x64 Assembly Language Step-by-Step makes quick work of programmable computing basics, the concepts of binary and hexadecimal number systems, the Intel x86/x64 computer architecture, and the process of Linux software development to dive deep into the x64 instruction set, memory addressing, procedures, macros, and interface to the C-language code libraries on which Linux is built.
You’ll also find:
    A set of free and open-source development and debugging tools you can download and put to use immediately
    Numerous examples woven throughout the book to illustrate the practical implementation of the ideas discussed within
    Practical tips on software design, coding, testing, and debugging
A one-stop resource for aspiring and practicing Intel assembly programmers, the latest edition of this celebrated text provides readers with an authoritative tutorial approach to x64 technology that’s ideal for self-paced instruction. ');

INSERT INTO book (name, author, publisher, description) VALUES('The Linux Programming Interface: A Linux and UNIX System Programming Handbook', 'Michael Kerrisk', 'No Starch Press', 'The Linux Programming Interface is the definitive guide to the Linux and UNIX programming interface—the interface employed by nearly every application that runs on a Linux or UNIX system.
In this authoritative work, Linux programming expert Michael Kerrisk provides detailed descriptions of the system calls and library functions that you need in order to master the craft of system programming, and accompanies his explanations with clear, complete example programs.

You’ll find descriptions of over 500 system calls and library functions, and more than 200 example programs, 88 tables, and 115 diagrams. You’ll learn how to:
–Read and write files efficiently
–Use signals, clocks, and timers
–Create processes and execute programs
–Write secure programs
–Write multithreaded programs using POSIX threads
–Build and use shared libraries
–Perform interprocess communication using pipes, message queues, shared memory, and semaphores
–Write network applications with the sockets API

While The Linux Programming Interface covers a wealth of Linux-specific features, including epoll, inotify, and the /proc file system, its emphasis on UNIX standards (POSIX.1-2001/SUSv3 and POSIX.1-2008/SUSv4) makes it equally valuable to programmers working on other UNIX platforms.  The Linux Programming Interface is the most comprehensive single-volume work on the Linux and UNIX programming interface, and a book that’s destined to become a new classic.
');

CREATE OR REPLACE PROCEDURE bookUpdateName(
  v_id int,
  v_name varchar(128)
)
AS
BEGIN
  UPDATE book SET name = v_name WHERE book_id = v_id;
END;
/
