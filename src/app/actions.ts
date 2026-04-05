"use server";

export type ContactFormState = {
  success: boolean;
  message: string;
};

export async function submitContactForm(
  prevState: ContactFormState,
  formData: FormData
): Promise<ContactFormState> {
  const name = formData.get("name") as string;
  const phone = formData.get("phone") as string;
  const email = formData.get("email") as string;
  const address = formData.get("address") as string;
  const service = formData.get("service") as string;
  const message = formData.get("message") as string;

  if (!name || !phone || !service) {
    return {
      success: false,
      message: "Please fill in all required fields.",
    };
  }

  // In production, integrate with an email service (Resend, SendGrid, etc.)
  // For now, log to console and return success
  console.log("New contact form submission:", {
    name,
    phone,
    email,
    address,
    service,
    message,
    timestamp: new Date().toISOString(),
  });

  // TODO: Add email integration
  // Example with Resend:
  // await resend.emails.send({
  //   from: "contact@spseptic.com",
  //   to: "info@spseptic.com",
  //   subject: `New Estimate Request from ${name}`,
  //   html: `...`,
  // });

  return {
    success: true,
    message: `Thank you, ${name}! We'll be in touch within 1 business day.`,
  };
}
